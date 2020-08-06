package com.dicoding.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.consumerapp.NoteAddUpdateActivity
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.entity.NoteModel
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter(private val activity: Activity) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listNotes = ArrayList<NoteModel>()
        set(listNotes) {
            this.listNotes.clear()
            if (listNotes.size > 0) {
                this.listNotes.addAll(listNotes)
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: NoteModel) {
            with(itemView) {
                tv_date.text = note.date
                tv_title.text = note.title
                tv_description.text = note.description
                setOnClickListener {
                    val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                    activity.startActivity(intent)
                }
            }
        }
    }
}