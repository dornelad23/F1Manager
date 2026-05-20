package com.example.f1manager

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class DiagonalPanelView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = Path()

        path.moveTo(0f, 0f)
        path.lineTo(width * 0.82f, 0f)

        // curva para DENTRO
        path.quadTo(
            width * 0.58f,
            height / 2f,
            width * 0.82f,
            height.toFloat()
        )

        path.lineTo(0f, height.toFloat())
        path.close()

        val gradient = LinearGradient(
            0f, 0f,
            width.toFloat(), 0f,
            Color.argb(245, 0, 0, 0),
            Color.argb(150, 0, 0, 0),
            Shader.TileMode.CLAMP
        )

        paint.shader = gradient
        canvas.drawPath(path, paint)
    }
}