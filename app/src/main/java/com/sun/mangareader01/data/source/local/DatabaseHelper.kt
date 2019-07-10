package com.sun.mangareader01.data.source.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sun.mangareader01.data.model.Manga
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    val mangas: List<Manga>
        get() {
            val mangas = ArrayList<Manga>()
            val db = writableDatabase
            val cursor = db.rawQuery(Tables.Manga.SELECT_ALL, null)
            cursor?.run {
                moveToFirst()
                while (!isAfterLast) {
                    mangas.add(exportManga(cursor))
                    moveToNext()
                }
                close()
            }
            return mangas
        }
    private val currentTime: String
        get() = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
            .format(Date())

    @Synchronized
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Tables.Manga.CREATE_TABLE)
    }

    @Synchronized
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL(Tables.Manga.DROP_TABLE)
        onCreate(db)
    }

    @Synchronized
    fun insertManga(manga: Manga): Boolean {
        val db = writableDatabase
        val values = createContentValues(manga)
        val result = db.insert(Tables.Manga.TABLE_NAME, null, values) != -1L
        db.close()
        return result
    }

    fun getManga(id: Int): Manga? {
        val db = readableDatabase
        val cursor = db.rawQuery(Tables.Manga.SELECT_BY_ID.format(id), null)
        return getManga(cursor)
    }

    fun getManga(slug: String): Manga? {
        val db = readableDatabase
        val cursor = db.rawQuery(Tables.Manga.SELECT_BY_SLUG.format(slug), null)
        return getManga(cursor)
    }

    @Synchronized
    fun updateManga(manga: Manga): Boolean {
        val values = createContentValues(manga)
        writableDatabase.run {
            replace(Tables.Manga.TABLE_NAME, null, values)
            close()
        }
        return true
    }

    @Synchronized
    fun deleteManga(manga: Manga): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            Tables.Manga.TABLE_NAME,
            Tables.Manga.COL_SLUG + "=?",
            arrayOf(manga.slug)
        ) != -1
        db.close()
        return result
    }

    private fun createContentValues(manga: Manga) = ContentValues().apply {
        put(Tables.Manga.COL_TITLE, manga.title)
        put(Tables.Manga.COL_SLUG, manga.slug)
        put(Tables.Manga.COL_SAVED, manga.saved.toString().toUpperCase())
        put(Tables.Manga.COL_UPDATED_AT, currentTime)
    }

    private fun exportManga(cursor: Cursor): Manga {
        with(cursor) {
            return Manga(
                title = getString(getColumnIndex(Tables.Manga.COL_TITLE)),
                slug = getString(getColumnIndex(Tables.Manga.COL_SLUG)),
                saved = getString(getColumnIndex(Tables.Manga.COL_SAVED))
                    == true.toString().toUpperCase()
            )
        }
    }

    private fun getManga(cursor: Cursor?): Manga? {
        var manga: Manga? = null
        cursor?.run {
            moveToFirst()
            manga = exportManga(this)
            close()
        }
        return manga
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "MangaReader01.db"
        private const val DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    object Tables {

        object Manga {
            const val TABLE_NAME = "manga"
            const val COL_ID = "id"
            const val COL_TITLE = "title"
            const val COL_SLUG = "slug"
            const val COL_SAVED = "saved"
            const val COL_UPDATED_AT = "updated_at"
            const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_SLUG + " TEXT UNIQUE NOT NULL, " +
                COL_SAVED + " BOOLEAN DEFAULT FALSE, " +
                COL_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)"
            const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
            const val SELECT_ALL = "SELECT * FROM $TABLE_NAME ORDER BY " +
                "$COL_UPDATED_AT DESC"
            private const val SELECT = "SELECT DISTINCT * FROM $TABLE_NAME"
            const val SELECT_BY_ID = SELECT + "WHERE $COL_ID = %d LIMIT 1"
            const val SELECT_BY_SLUG = SELECT + "WHERE $COL_SLUG = %s LIMIT 1"
        }
    }
}
