package com.dicoding.consumerapp.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import android.util.Log
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.DATE
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.DESCRIPTION
import com.dicoding.consumerapp.db.DatabaseContract.NotesColumn.Companion.TITLE
import com.dicoding.consumerapp.entity.NoteModel

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<NoteModel> {
        val notes = ArrayList<NoteModel>()

        while (notesCursor!!.moveToNext()) {
            Log.d("MAPPING_HELPER", notesCursor.isLast.toString())
            notesCursor.run {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val title = getString(getColumnIndexOrThrow(TITLE))
                val description = getString(getColumnIndexOrThrow(DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DATE))
                notes.add(NoteModel(id, title, description, date))
            }
        }

        return notes
    }

    fun mapCursorToObject(noteCursor: Cursor?): NoteModel {
        var note = NoteModel()
        noteCursor?.run {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val title = getString(getColumnIndexOrThrow(TITLE))
            val description = getString(getColumnIndexOrThrow(DESCRIPTION))
            val date = getString(getColumnIndexOrThrow(DATE))
            note = NoteModel(id, title, description, date)
        }

        return note
    }
}