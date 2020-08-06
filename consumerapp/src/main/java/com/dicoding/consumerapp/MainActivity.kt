package com.dicoding.consumerapp

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.adapter.NotesAdapter
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.CONTENT_URI
import com.dicoding.consumerapp.entity.NoteModel
import com.dicoding.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NotesAdapter

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NotesAdapter(this)

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        rv_notes.adapter = adapter

        fab_add.setOnClickListener {
            val intent = Intent(this, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val observer = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        if (savedInstanceState == null) {
            loadAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<NoteModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    private fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            progressbar.visibility = View.GONE
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList<NoteModel>()
                Snackbar.make(rv_notes, "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
