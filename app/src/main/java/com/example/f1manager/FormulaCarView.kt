package com.example.f1manager

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FormulaCarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var motorNivel = 1
    var aeroNivel = 1
    var pneuNivel = 1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2
        val cy = h / 2 + 20

        // sombra
        paint.color = Color.argb(150, 0, 0, 0)
        canvas.drawOval(cx - 270, cy + 80, cx + 270, cy + 120, paint)

        // pneus traseiros grandes
        drawWheel(canvas, cx - 170, cy + 35, 58f + pneuNivel * 3)
        drawWheel(canvas, cx + 170, cy + 35, 58f + pneuNivel * 3)

        // pneus dianteiros
        drawWheel(canvas, cx - 230, cy + 20, 45f + pneuNivel * 2)
        drawWheel(canvas, cx + 230, cy + 20, 45f + pneuNivel * 2)

        // corpo central
        paint.color = Color.rgb(180 + motorNivel * 10, 0, 18)
        val body = Path()
        body.moveTo(cx - 210, cy)
        body.quadTo(cx - 80, cy - 70, cx + 80, cy - 70)
        body.quadTo(cx + 210, cy, cx + 210, cy + 35)
        body.lineTo(cx - 210, cy + 35)
        body.close()
        canvas.drawPath(body, paint)

        // bico fino
        paint.color = Color.rgb(220, 0, 25)
        val nose = Path()
        nose.moveTo(cx - 80, cy - 20)
        nose.lineTo(cx - 300, cy + 5)
        nose.lineTo(cx - 80, cy + 28)
        nose.close()
        canvas.drawPath(nose, paint)

        // cockpit preto
        paint.color = Color.rgb(15, 15, 18)
        canvas.drawOval(cx - 45, cy - 85, cx + 45, cy - 20, paint)

        // halo
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        paint.color = Color.WHITE
        canvas.drawArc(cx - 58, cy - 95, cx + 58, cy - 5, 200f, 140f, false, paint)
        paint.style = Paint.Style.FILL

        // asa dianteira
        paint.color = Color.WHITE
        canvas.drawRoundRect(cx - 340, cy - 20, cx - 255, cy + 22, 8f, 8f, paint)
        paint.color = Color.RED
        canvas.drawRect(cx - 335, cy + 8, cx - 260, cy + 15, paint)

        // asa traseira maior com aero
        paint.color = Color.WHITE
        canvas.drawRoundRect(cx + 215, cy - 95 - aeroNivel * 3, cx + 335, cy - 60, 8f, 8f, paint)

        paint.color = Color.RED
        canvas.drawRect(cx + 225, cy - 75 - aeroNivel * 3, cx + 325, cy - 66 - aeroNivel * 3, paint)

        // detalhe central
        paint.color = Color.WHITE
        canvas.drawRoundRect(cx - 30, cy - 65, cx + 30, cy + 35, 18f, 18f, paint)

        paint.color = Color.RED
        canvas.drawRect(cx - 8, cy - 60, cx + 8, cy + 35, paint)

        // fogo/energia no motor em nível alto
        if (motorNivel >= 4) {
            paint.color = Color.rgb(255, 120, 0)
            canvas.drawCircle(cx + 245, cy + 20, 14f, paint)
        }
    }

    private fun drawWheel(canvas: Canvas, x: Float, y: Float, r: Float) {
        paint.color = Color.BLACK
        canvas.drawCircle(x, y, r, paint)

        paint.color = Color.rgb(35, 35, 35)
        canvas.drawCircle(x, y, r * 0.72f, paint)

        paint.color = Color.rgb(180, 180, 180)
        canvas.drawCircle(x, y, r * 0.28f, paint)
    }

    fun atualizarCarro(motor: Int, aero: Int, pneu: Int) {
        motorNivel = motor
        aeroNivel = aero
        pneuNivel = pneu
        invalidate()
    }
}