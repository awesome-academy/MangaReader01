package com.sun.mangareader01.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.renderscript.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class BlurTransformation(
    private val context: Context,
    private val radius: Float
) : BitmapTransformation() {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(RSRuntimeException::class)
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val renderScript = RenderScript.create(context)
        val input = Allocation.createFromBitmap(
            renderScript,
            toTransform,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT
        )
        val output = Allocation.createTyped(renderScript, input.type)
        val blur = ScriptIntrinsicBlur.create(
            renderScript,
            Element.U8_4(renderScript)
        ).apply {
            setInput(input)
            setRadius(radius)
            forEach(output)
        }
        output.copyTo(toTransform)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RenderScript.releaseAllContexts()
        } else {
            renderScript?.destroy()
        }
        input?.destroy()
        output?.destroy()
        blur?.destroy()
        return toTransform
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }
}
