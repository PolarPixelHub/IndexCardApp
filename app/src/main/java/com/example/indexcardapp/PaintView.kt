package com.example.indexcardapp

import android.util.DisplayMetrics
import androidx.compose.ui.graphics.Color
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

data class FingerPath(
    var color: Int,
    var strokeWidth: Int,
    var path: Path
)

fun Color.toArgb(): Int {
    val alpha = (this.alpha * 255).toInt() shl 24
    val red = (this.red * 255).toInt() shl 16
    val green = (this.green * 255).toInt() shl 8
    val blue = (this.blue * 255).toInt()
    return alpha or red or green or blue
}

class PaintView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        const val BRUSH_SIZE = 10
        val DEFAULT_COLOR = Color.Black.toArgb()
        val DEFAULT_BG_COLOR = Color.White.toArgb()
        const val TOUCH_TOLERANCE = 4f
    }

    private var mX = 0f
    private var mY = 0f
    private lateinit var mPath: Path
    private val mPaint: Paint = Paint()
    private val paths = ArrayList<FingerPath>()
    private var currentColor: Int = DEFAULT_COLOR
    private var backgroundColor: Int = DEFAULT_BG_COLOR
    private var strokeWidth: Int = BRUSH_SIZE
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private val mBitmapPaint: Paint = Paint(Paint.DITHER_FLAG)

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = DEFAULT_COLOR
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.xfermode = null
        mPaint.alpha = 0xff
    }

    private fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSH_SIZE
    }

    fun clear() {
        backgroundColor = DEFAULT_BG_COLOR
        paths.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        mCanvas.drawColor(backgroundColor)

        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mPaint.maskFilter = null

            mCanvas.drawPath(fp.path, mPaint)
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = FingerPath(currentColor, strokeWidth, mPath)
        paths.add(fp)

        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
                performClick()  // Call performClick to register click event
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init(resources.displayMetrics)
    }

    fun getDrawingBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }


}



