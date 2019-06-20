package com.sun.mangareader01.utils

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress(
    "unused",
    "UNUSED_ANONYMOUS_PARAMETER",
    "MemberVisibilityCanBePrivate"
)
class TouchImageView : AppCompatImageView,
    View.OnTouchListener {

    private val gestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val velocity = sqrt(velocityX.pow(2) + velocityY.pow(2))
                if (velocity > 10000) {
                    this@TouchImageView.visibility = View.INVISIBLE
                    this@TouchImageView.setImageDrawable(null)
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        }

    private var destMatrix: Matrix? = null
    private var mode = NONE

    private var lastPoint = PointF()
    private var startPoint = PointF()

    private val minScale = .1F
    private var maxScale = 10F

    private var m: FloatArray? = null
    private var saveScale = 1F
    private var viewWidth = 0
    private var viewHeight = 0

    private var origWidth = 0F
    private var origHeight = 0F
    private var oldMeasuredWidth = 0
    private var oldMeasuredHeight = 0

    private var scaleDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    constructor(context: Context) : super(context) {
        sharedConstructing(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, gestureListener)
        destMatrix = Matrix()
        m = FloatArray(9)
        imageMatrix = destMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        scaleDetector?.onTouchEvent(event)
        gestureDetector?.onTouchEvent(event)
        val eventPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onTouchEvent(eventPoint)
            MotionEvent.ACTION_MOVE -> onMoveEvent(eventPoint)
            MotionEvent.ACTION_UP -> onUnTouchEvent(eventPoint)
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
        }
        imageMatrix = destMatrix
        invalidate()
        return true
    }

    private fun onTouchEvent(point: PointF) {
        lastPoint.set(point)
        startPoint.set(lastPoint)
        mode = DRAG
    }

    private fun onMoveEvent(point: PointF) {
        if (mode == DRAG) {
            val deltaX = point.x - lastPoint.x
            val deltaY = point.y - lastPoint.y
            destMatrix?.postTranslate(deltaX, deltaY)
            fixTrans()
            lastPoint.set(point)
        }
    }

    private fun onUnTouchEvent(point: PointF) {
        mode = NONE
        val xDiff = Math.abs(point.x - startPoint.x).toInt()
        val yDiff = Math.abs(point.y - startPoint.y).toInt()
        if (xDiff < CLICK && yDiff < CLICK) performClick()
    }

    fun setMaxZoom(x: Float) {
        maxScale = x
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        Log.e(this.javaClass.simpleName, "$viewWidth x $viewHeight")
        reset()
    }

    private fun reset() {
        saveScale = 1F
    }

    private inner class ScaleListener :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor

            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }

            if (origWidth * saveScale <= viewWidth ||
                origHeight * saveScale <= viewHeight
            ) {
                destMatrix?.postScale(
                    mScaleFactor, mScaleFactor,
                    viewWidth / 2F, viewHeight / 2F
                )
            } else {
                destMatrix?.postScale(
                    mScaleFactor, mScaleFactor,
                    detector.focusX, detector.focusY
                )
            }

            fixTrans()
            return true
        }
    }

    internal fun fixTrans() {
        destMatrix?.getValues(m)

        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]

        val fixTransX =
            getFixTrans(transX, viewWidth.toFloat(), origWidth * saveScale)
        val fixTransY =
            getFixTrans(transY, viewHeight.toFloat(), origHeight * saveScale)

        if (fixTransX != 0F || fixTransY != 0F) {
            destMatrix?.postTranslate(fixTransX, fixTransY)
        }
    }

    internal fun getFixTrans(
        trans: Float,
        viewSize: Float,
        contentSize: Float
    ): Float {
        val minTrans: Float
        val maxTrans: Float

        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }

        return when {
            trans < minTrans -> -trans + minTrans
            trans > maxTrans -> -trans + maxTrans
            else -> 0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (oldMeasuredHeight == viewWidth &&
            oldMeasuredHeight == viewHeight ||
            viewWidth == 0 || viewHeight == 0 ||
            drawable == null ||
            drawable.intrinsicWidth == 0 ||
            drawable.intrinsicHeight == 0
        ) return

        oldMeasuredHeight = viewHeight
        oldMeasuredWidth = viewWidth

        if (saveScale == 1F) {
            val bmWidth = drawable.intrinsicWidth
            val bmHeight = drawable.intrinsicHeight

            val scaleX = viewWidth.toFloat() / bmWidth.toFloat()
            val scaleY = viewHeight.toFloat() / bmHeight.toFloat()

            val scale = Math.min(scaleX, scaleY)

            val redundantX = viewWidth.toFloat() - scale * bmWidth.toFloat()
            val redundantY = viewHeight.toFloat() - scale * bmHeight.toFloat()

            destMatrix?.apply {
                setScale(scale, scale)
                postTranslate(redundantX / 2F, redundantY / 2F)
            }
            origWidth = viewWidth - redundantX
            origHeight = viewHeight - redundantY
            imageMatrix = destMatrix
        }
        fixTrans()
    }

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 4
        private const val CLICK = 3
    }
}
