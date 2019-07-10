package com.sun.mangareader01.utils.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

class BlurTransformation constructor(
    private val radius: Int = MAX_RADIUS,
    private val sampling: Int = DEFAULT_DOWN_SAMPLING
) : BitmapTransformation() {

    override fun transform(
        context: Context, pool: BitmapPool,
        toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        val scaledWidth = toTransform.width / sampling
        val scaledHeight = toTransform.height / sampling
        val bitmap = pool.get(scaledWidth, scaledHeight, Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint().apply { flags = Paint.FILTER_BITMAP_FLAG }
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return FastBlur.blur(bitmap, radius, true)
    }

    override fun equals(other: Any?) = other is BlurTransformation &&
        other.radius == radius && other.sampling == sampling

    override fun hashCode() = ID.hashCode() + radius * 1000 + sampling * 10

    override fun updateDiskCacheKey(messageDigest: MessageDigest) =
        messageDigest.update((ID + radius + sampling).toByteArray(Key.CHARSET))

    companion object {
        private const val VERSION = 1
        private const val ID =
            "com.sun.mangareader01.utils.blur.BlurTransformation.$VERSION"
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }
}
