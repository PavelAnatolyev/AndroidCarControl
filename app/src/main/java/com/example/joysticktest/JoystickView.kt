package com.example.joysticktest

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

class JoystickView : SurfaceView, SurfaceHolder.Callback, View.OnTouchListener {
    private var centerX: Float = 0F
    private var centerY: Float = 0F
    private var baseRadius: Float = 0F
    private var hatRadius: Float = 0F
    private var joystickListener: JoystickListener? = null
    private val ration: Int = 5

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        holder.addCallback(this)
        setOnTouchListener(this)
        if (context is JoystickListener) {
            joystickListener = context
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        setupDimensions()
        drawJoystick(centerX, centerY)
    }

    private fun setupDimensions() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        baseRadius = Math.min(width, height).toFloat() / 3
        hatRadius = Math.min(width, height).toFloat() / 6
    }

    private fun drawJoystick(newX: Float, newY: Float) {
        if (holder.surface.isValid) {
            val myCanvas = holder.lockCanvas()
            val colors = Paint()
            myCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC)
            colors.setARGB(255, 50, 50, 50)
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors)
            colors.setARGB(255, 0, 0, 255)
            myCanvas.drawCircle(newX, newY, hatRadius, colors)
            holder.unlockCanvasAndPost(myCanvas)
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (this.equals(view)) {
            if (event!!.action != MotionEvent.ACTION_UP) {
                val displacement: Float = Math.sqrt(Math.pow((event.x - centerX).toDouble(), 2.0)
                        + Math.pow((event.y - centerY).toDouble(), 2.0)).toFloat()
                if (displacement < baseRadius) {
                    drawJoystick(event.x, event.y)
                    joystickListener!!.onJoystickMoved((event.x - centerX) / baseRadius, (event.y - centerY) / baseRadius, id)
                } else {
                    val ratio: Float = baseRadius / displacement
                    val constrainedX: Float = centerX + (event.x - centerX) * ratio
                    val constrainedY: Float = centerY + (event.y - centerY) * ratio
                    drawJoystick(constrainedX, constrainedY)
                    joystickListener!!.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, id)
                }
            } else {
                drawJoystick(centerX, centerY)
                joystickListener!!.onJoystickMoved(0F, 0F, id)
            }
        }
        return true;
    }

    public interface JoystickListener {
        fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int)
    }
}
