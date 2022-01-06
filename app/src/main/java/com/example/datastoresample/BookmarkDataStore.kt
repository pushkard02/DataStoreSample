package com.example.datastoresample

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class BookmarkDataStore(context: Context) {
    private val appContext = context.applicationContext
    private val Context.dataStore: DataStore<Bookmark> by dataStore(
        fileName = "bookmark.proto",
        serializer = BookmarkSerializer
    )

    val bookmark = appContext.dataStore.data
        .map { bookmarkSchema ->
            bookmarkSchema.bookmark
        }

    suspend fun saveBookmark(bookmark: String) {
        appContext.dataStore.updateData { currentBookmark ->
            currentBookmark.toBuilder()
                .setBookmark(bookmark)
                .build()
        }
    }


    object BookmarkSerializer : Serializer<Bookmark> {
        override suspend fun readFrom(input: InputStream): Bookmark {
            try {
                return Bookmark.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(
            t: Bookmark,
            output: OutputStream
        ) = t.writeTo(output)

        override val defaultValue: Bookmark
            get() = Bookmark.getDefaultInstance()
    }
}