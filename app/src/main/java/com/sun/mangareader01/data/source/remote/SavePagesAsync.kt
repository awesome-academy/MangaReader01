package com.sun.mangareader01.data.source.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import com.sun.mangareader01.data.source.local.SavePagesCallback
import com.sun.mangareader01.utils.Extensions.warrantExisted
import com.sun.mangareader01.utils.Extensions.warrantNotExisted
import com.sun.mangareader01.utils.FileHelpers
import com.sun.mangareader01.utils.FileHelpers.DirFormat.PATH_SAVE_CHAPTER
import com.sun.mangareader01.utils.Helpers
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class SavePagesAsync(
    context: Context,
    private val callback: SavePagesCallback
) : AsyncTask<List<String>, Int, Boolean>() {

    private var exception: Exception? = null
    private val appPath = FileHelpers.getFilePath(context)

    override fun onPreExecute() {
        super.onPreExecute()
        File(appPath).warrantExisted()
    }

    override fun doInBackground(vararg pageUrls: List<String>): Boolean =
        try {
            val urls = pageUrls.first()
            val mangaSlug = Helpers.getMangaSlug(urls.first())
            val chapterSlug = Helpers.getChapterSlug(urls.first())
            for (index in 0 until urls.size) {
                val url = urls[index]
                val fileName = Uri.parse(url).lastPathSegment
                val saveDir =
                    PATH_SAVE_CHAPTER.format(appPath, mangaSlug, chapterSlug)
                saveBitmapToFile(getBitmap(url)!!, saveDir, fileName!!)
                publishProgress(index + 1)
            }
            true
        } catch (exception: Exception) {
            this.exception = exception
            false
        }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values.first()?.let { callback.onUpdate(it) }
    }

    override fun onPostExecute(isSuccessful: Boolean) =
        callback.onComplete(isSuccessful)

    private fun getBitmap(url: String): Bitmap? {
        val connection = URL(url).openConnection().apply {
            doInput = true
            connect()
        }
        val inputStream = connection.getInputStream()
        val option = BitmapFactory.Options().apply {
            inPreferredConfig = Config.RGB_565
        }
        val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
        inputStream.close()
        return bitmap
    }

    private fun saveBitmapToFile(
        bitmap: Bitmap,
        saveDir: String,
        fileName: String
    ) {
        File(saveDir).warrantExisted()
        val imageFile = File(saveDir, fileName).warrantNotExisted()
        val out = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_COMPRESS, out)
        out.flush()
        out.close()
    }

    companion object {
        private const val QUALITY_COMPRESS = 80
    }
}
