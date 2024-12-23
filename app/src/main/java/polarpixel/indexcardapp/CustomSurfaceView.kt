package polarpixel.indexcardapp

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.util.Log
import android.view.SurfaceHolder

class CustomSurfaceView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    init {
        holder.addCallback(this)
        isClickable = true  // Ensure the view can respond to clicks

        // Set touch listener to handle drawing
        setOnTouchListener { v, event ->
            handleCanvasTouch(event)
            v.performClick()  // Ensure performClick is called on touch events
            true
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()  // Call the superclass method
    }

    private fun handleCanvasTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y

                val paint = Paint().apply {
                    color = Color.BLACK
                    strokeWidth = 5f
                    isAntiAlias = true
                }

                // Lock the canvas from the SurfaceHolder
                val surfaceHolder = holder
                val canvas = surfaceHolder.lockCanvas() ?: return false

                try {
                    // Draw on the canvas
                    canvas.drawCircle(x, y, 10f, paint)
                    Log.d("CustomSurfaceView", "Drawing at x: $x, y: $y with radius 10f")
                } catch (e: Exception) {
                    Log.e("CustomSurfaceView", "Error while drawing: ${e.message}")
                } finally {
                    // Unlock and post the canvas to refresh
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }

                return true
            }
        }
        return false
    }

    // SurfaceHolder.Callback methods
    override fun surfaceCreated(holder: SurfaceHolder) {
        // Initialize resources if needed
        Log.d("CustomSurfaceView", "Surface created")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("CustomSurfaceView", "Surface changed")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("CustomSurfaceView", "Surface destroyed")
    }
}



