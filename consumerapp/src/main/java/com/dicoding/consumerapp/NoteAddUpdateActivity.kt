package com.dicoding.consumerapp

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.CONTENT_URI
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.DATE
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.DESCRIPTION
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.TITLE
import com.dicoding.consumerapp.entity.NoteModel
import com.dicoding.consumerapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var note: NoteModel? = null
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_NOTE = "extra_note"
        private const val DELETE_DIALOG = 100
        private const val CLOSE_DIALOG = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            isEdit = true
        } else {
            note = NoteModel()
        }

        val actionBarTitle: String
        val btnTitle: String
        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            uriWithId = Uri.parse("$CONTENT_URI/${note?.id}")
            val cursor = contentResolver?.query(uriWithId, null, null, null, null)
            if (cursor != null) {
                note = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }

            edt_title.setText(note?.title)
            edt_description.setText(note?.description)
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        submit.text = btnTitle

        submit.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(DELETE_DIALOG)
            android.R.id.home -> showAlertDialog(CLOSE_DIALOG)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(CLOSE_DIALOG)
    }

    private fun showAlertDialog(type: Int) {
        val isClosed = type == CLOSE_DIALOG
        val dialogTitle: String
        val dialogMessage: String

        if (isClosed) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogTitle = "Hapus Note"
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, which ->
                if (isClosed) {
                    finish()
                } else {
                    contentResolver?.delete(uriWithId, null, null)
                    Toast.makeText(this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, which -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.submit) {
            val title = edt_title.text.toString()
            val desc = edt_description.text.toString()

            var isValid = true
            if (title.isEmpty()) {
                edt_title.error = "Title Tidak boleh kosong"
                isValid = false
            }

            if (desc.isEmpty()) {
                edt_description.error = "Desc tidak boleh kosong"
                isValid = false
            }

            if (!isValid) return

            val values = ContentValues()
            values.put(TITLE, title)
            values.put(DESCRIPTION, desc)
            if (isEdit) {
                contentResolver?.update(uriWithId, values, null, null)
                Toast.makeText(this, "Satu item berhasil di update", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                values.put(DATE, getCurrentDate())
                contentResolver?.insert(CONTENT_URI, values)
                Toast.makeText(this, "berhasil menambahkan satu item", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}