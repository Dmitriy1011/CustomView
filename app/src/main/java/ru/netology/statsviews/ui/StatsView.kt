package ru.netology.statsview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.statsviews.R
import ru.netology.statsviews.ui.utils.AndroidUtil
import kotlin.math.min
import kotlin.random.Random


//JvmOverloads для получения набора конструкторов
class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes
) {

    private var textSize = AndroidUtil.dp(context, 20).toFloat()
    private var lineWidth = AndroidUtil.dp(context, 5)
    private var colors = emptyList<Int>()
    private var backgroundColor = 0


    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()

            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor()),
            )

            backgroundColor = getColor(R.styleable.StatsView_backgroundColor, 0)
        }

        backgroundColor = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorControlNormal)).use {
            it.getColor(0,0)
        }
    }

    private var radius = 0F
    private var center = PointF(0F, 0F)

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

    //кисть для отрисовки дуги
    private var paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth.toFloat()
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    //кисть для отрисовки текста
    private var paintText = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }


    var data: List<Float> = listOf(
        0.25F,
        0.25F,
        0.25F,
        0.25F
    )

    private var oval = RectF(0F, 0F, 0F, 0F)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = backgroundColor
        canvas.drawCircle(center.x, center.y, radius, paint)

        if (data.isEmpty()) {
            canvas.drawText(
                "%.2f%%".format(0),
                center.x,
                center.y + paintText.textSize / 4,
                paintText
            )
            return
        }

        var startAngle = -90F
        var firstColor = 0
        data.forEachIndexed { index, item ->
            val angle = item * 360F
            paint.color = colors.getOrElse(index) { generateRandomColor() }

            if (firstColor == 0) {
                firstColor = paint.color
            }

            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }

        paint.color = firstColor
        canvas.drawPoint(center.x, (lineWidth / 2).toFloat(), paint)

        canvas.drawText(
            "%.2f%%".format(data.sum() * 100),
            center.x,
            center.y + paintText.textSize / 4,
            paintText
        )
    }
}