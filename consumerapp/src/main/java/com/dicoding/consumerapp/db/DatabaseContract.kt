package com.dicoding.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract {
    companion object {
        const val AUTHORITY = "com.dicoding.mynotesapp"
        const val SCHEME = "content"
    }

    internal class NotesColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"

            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}