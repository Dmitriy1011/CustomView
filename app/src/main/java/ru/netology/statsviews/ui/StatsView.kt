package ru.netology.statsview.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
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

    private var progress = 0F
    private var valueAnimator: ValueAnimator? = null

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

        backgroundColor =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorControlNormal))
                .use {
                    it.getColor(0, 0)
                }
    }

    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)

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


    var data: List<Float> = emptyList()
        set(value) {
            field = value
            update()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {

        if (data.isEmpty()) {
            canvas.drawText(
                "%.2f%%".format(0),
                center.x,
                center.y + paintText.textSize / 4,
                paintText
            )
            return
        }

        val sum = data.sum()

        canvas.drawText(
            "%.2f%%".format(sum * progress * 100),
            center.x,
            center.y + paintText.textSize / 4,
            paintText
        )

        var max = sum * 360F
        var startAngle = -90F
        val progressAngle = progress * 360F

        if (progressAngle > max) {
            data.forEachIndexed { index, item ->
                val angle = item * 360F
                paint.color = colors.getOrElse(index) { generateRandomColor() }
                canvas.drawArc(oval, startAngle, angle, false, paint)
                startAngle += angle
            }
            return
        }

        var filled = 0F

        data.forEachIndexed { index, item ->
            val angle = item * 360F
            paint.color = colors.getOrElse(index) { generateRandomColor() }

            canvas.drawArc(oval, startAngle, progressAngle - filled, false, paint)
            startAngle += angle
            filled += angle

            if(filled > progressAngle) return
        }
    }

    private fun update() {
        valueAnimator?.let {
            it.removeAllListeners()
            it.cancel()
        }

        progress = 0F

        valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener { animator ->
                progress = animator.animatedValue as Float
                invalidate()
            }
            duration = 5_000
            interpolator = LinearInterpolator()
        }.also {
            it.start()
        }
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}