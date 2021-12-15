package com.xcellapps.test2313

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.graphics.withClip

class LPELayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var outerBorderWidth = 0
    private var drawOuterBorder = false

    private var mainBorderWidth = 0
    private var drawMainBorder = false

    private var innerBorderWidth = 0
    private var drawInnerBorder = false

    private var innerPadding = 4.0f

    private var arrowHeight = 0
    private var arrowWidth = 0

    private var outerBorderPaint = Paint()
    private var mainBorderPaint = Paint()
    private var innerBorderPaint = Paint()

    private var outerBorderPath = Path()
    private var mainBorderPath = Path()
    private var innerBorderPath = Path()
    private var mArrowPath = Path()
    private var mainArrowPath = Path()
    private var innerArrowPath = Path()

    private val arrowStartPoint = PointF(0f,0f)

    init {
        readAttributes(attrs, defStyleAttr)
    }

    private fun readAttributes(attrs: AttributeSet?, defStyleAttr: Int) {

        //todo:: read all attributes (outer border width, inner border width etc)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LPELayout, defStyleAttr, 0)
        outerBorderWidth = typedArray.getDimensionPixelSize(R.styleable.LPELayout_outerBorderWidth, 0)
        val outerBorderColor = typedArray.getColor(R.styleable.LPELayout_outerBorderColor, Color.TRANSPARENT)
        drawOuterBorder = outerBorderWidth > 0

        val mainBorderColor = typedArray.getColor(R.styleable.LPELayout_mainBorderColor, Color.TRANSPARENT)
        mainBorderWidth = typedArray.getDimensionPixelSize(R.styleable.LPELayout_mainBorderWidth, 0)
        drawMainBorder = mainBorderWidth > 0

        val innerBorderColor = typedArray.getColor(R.styleable.LPELayout_innerBorderColor, Color.TRANSPARENT)
        innerBorderWidth = typedArray.getDimensionPixelSize(R.styleable.LPELayout_innerBorderWidth, 0)
        drawInnerBorder = innerBorderWidth > 0

        arrowHeight = typedArray.getDimensionPixelSize(R.styleable.LPELayout_arrowHeight, 24)
        arrowWidth = typedArray.getDimensionPixelSize(R.styleable.LPELayout_arrowWidth, 24)

        typedArray.recycle()


        outerBorderPaint.apply {
            color = outerBorderColor
            strokeWidth = outerBorderWidth.toFloat()
            style = Paint.Style.STROKE
        }

        mainBorderPaint.apply {
            color = mainBorderColor
            strokeWidth = mainBorderWidth.toFloat()
            style = Paint.Style.STROKE
        }

        innerBorderPaint.apply {
            color = innerBorderColor
            strokeWidth = innerBorderWidth.toFloat()
            style = Paint.Style.STROKE
        }

        mArrowPath.moveTo(0f, 0f)
        mArrowPath.lineTo(arrowWidth/2f, arrowHeight.toFloat())
        mArrowPath.lineTo(arrowWidth.toFloat(), 0f)

        mainArrowPath.moveTo(0f, 0f)
        mainArrowPath.lineTo((arrowWidth - outerBorderWidth)/2f, (arrowHeight - outerBorderWidth).toFloat())
        mainArrowPath.lineTo((arrowWidth - outerBorderWidth).toFloat(), 0f)

        innerArrowPath.moveTo(0f, 0f)
        innerArrowPath.lineTo((arrowWidth - outerBorderWidth - mainBorderWidth)/2f, (arrowHeight - outerBorderWidth - mainBorderWidth).toFloat())
        innerArrowPath.lineTo((arrowWidth - outerBorderWidth - mainBorderWidth).toFloat(), 0f)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            (measuredWidth + (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding)).toInt(),
            (measuredHeight + (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding) + arrowHeight).toInt()
        )
    }

    fun setStartPoint(startPoint: PointF) {
        arrowStartPoint.set(startPoint)
    }

    override fun onDraw(canvas: Canvas?) {
        outerBorderPath.reset()
        mainBorderPath.reset()
        innerBorderPath.reset()


        ///let's draw our content first
        canvas?.let { drawingCanvas ->
            outerBorderPath.addRect(outerBorderWidth.toFloat(),outerBorderWidth.toFloat(),width.toFloat() - outerBorderWidth, (height - arrowHeight - outerBorderWidth).toFloat(), Path.Direction.CW)
            outerBorderPath.addPath(mArrowPath, width.toFloat() - arrowWidth - 100,
                (height - arrowHeight - outerBorderWidth).toFloat()
            )
            drawingCanvas.drawPath(outerBorderPath, outerBorderPaint)
            mainBorderPath.addRect(outerBorderWidth + mainBorderWidth.toFloat(),
            outerBorderWidth + mainBorderWidth.toFloat(),
                width.toFloat() - outerBorderWidth - mainBorderWidth,
                (height - arrowHeight - outerBorderWidth - mainBorderWidth).toFloat(),
                Path.Direction.CW
                )

            mainBorderPath.addPath(mainArrowPath, width.toFloat() - arrowWidth + (outerBorderWidth/2) - 100,
                (height - arrowHeight - outerBorderWidth - mainBorderWidth).toFloat()
                )
            drawingCanvas.drawPath(mainBorderPath, mainBorderPaint)

            innerBorderPath.addRect(outerBorderWidth + mainBorderWidth + innerBorderWidth.toFloat(),
                outerBorderWidth + mainBorderWidth*1f + innerBorderWidth,
                width.toFloat() - outerBorderWidth - mainBorderWidth*1f - innerBorderWidth,
                (height - arrowHeight - outerBorderWidth - mainBorderWidth*1f - innerBorderWidth).toFloat(),
                Path.Direction.CW
                )
            innerBorderPath.addPath(innerArrowPath, width.toFloat() - arrowWidth + (outerBorderWidth + mainBorderWidth)/2 - 100,
                (height - arrowHeight - outerBorderWidth - mainBorderWidth - innerBorderWidth).toFloat()
                )
            drawingCanvas.drawPath(innerBorderPath, innerBorderPaint)
        }

        ///translate canvas to the child can be drawn now
        canvas?.save()
//        canvas?.translate((2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding), (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding))
        super.onDraw(canvas)
        canvas?.restore()
    }
}