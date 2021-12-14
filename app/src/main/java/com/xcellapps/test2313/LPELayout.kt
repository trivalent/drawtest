package com.xcellapps.test2313

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

class LPELayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var outerBorderWidth = 0
    private var drawOuterBorder = false

    private var mainBorderWidth = 0
    private var drawMainBorder = false

    private var innerBorderWidth = 2.0f
    private var innerBorderColor = Color.GRAY

    private var innerPadding = 4.0f

    private var arrowHeight = 0
    private var arrowWidth = 0

    private var outerBorderPaint = Paint()
    private var mainBorderPaint = Paint()
    private var innerBorderPaint = Paint()

    private var outerBorderPath = Path()
    private var mainBorderPath = Path()
    private var innerBorderPath = Path()

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
            strokeWidth = innerBorderWidth
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        /// we must now have measured width available to us
        if(measuredWidth > 0 && measuredHeight > 0) {
            Log.d("LPELayout", "Measured width $measuredWidth and Measured height $measuredHeight")
            setMeasuredDimension(
                (measuredWidth + (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding)).toInt(),
                (measuredHeight + (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding) + arrowHeight).toInt()
            )
            Log.d("LPELayout", "Updated measured width $measuredWidth and measured height $measuredHeight")
        }
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
            var xOffset = (outerBorderWidth/2).toFloat()
            var yOffset = (outerBorderWidth/2).toFloat()

            val viewHeight = height.toFloat()
            val viewWidth = width.toFloat()
            ///decide the position of the arrow
            val startPosition = 50f

            if(drawOuterBorder) {
                outerBorderPath.moveTo(0f, yOffset)   //move to beginning
                //top line
                outerBorderPath.lineTo(viewWidth - xOffset, yOffset)
                //right line
                outerBorderPath.lineTo(viewWidth - xOffset, viewHeight - arrowHeight)
                //bottom first part
                outerBorderPath.lineTo(viewWidth - startPosition, viewHeight - arrowHeight)
                //arrow right part
                outerBorderPath.lineTo(viewWidth - (startPosition + arrowWidth / 2), viewHeight - outerBorderWidth)
                //arrow left part
                outerBorderPath.lineTo(
                    viewWidth - (startPosition + arrowWidth),
                    viewHeight - arrowHeight
                )
                //bottom second part
                outerBorderPath.lineTo(xOffset, viewHeight - arrowHeight)
                //left line
                outerBorderPath.lineTo(xOffset, yOffset)

                drawingCanvas.drawPath(outerBorderPath, outerBorderPaint)
            }

//            xOffset = outerBorderWidth/2 + (mainBorderWidth/2).toFloat()
//            yOffset = outerBorderWidth + (mainBorderWidth/2).toFloat()


            if(drawMainBorder) {
                mainBorderPath.moveTo((outerBorderWidth).toFloat(),
                    (outerBorderWidth + mainBorderWidth/2).toFloat()
                )   //move to beginning
                //top line
                mainBorderPath.lineTo(viewWidth - outerBorderWidth - mainBorderWidth/2, outerBorderWidth.toFloat() + mainBorderWidth/2)
                //right line
                mainBorderPath.lineTo(viewWidth - outerBorderWidth - mainBorderWidth/2, viewHeight - arrowHeight - outerBorderWidth/2 - mainBorderWidth/2)

                //bottom first part
                mainBorderPath.lineTo(viewWidth - outerBorderWidth/4 - mainBorderWidth/2 - startPosition, viewHeight - arrowHeight - outerBorderWidth/2 - mainBorderWidth/2)

                //arrow right part
//                 mainBorderPath.lineTo(viewWidth - outerBorderWidth - mainBorderWidth/2  - (startPosition + arrowWidth/2), viewHeight - outerBorderWidth)
                 mainBorderPath.lineTo(viewWidth - (startPosition + arrowWidth / 2), viewHeight - outerBorderWidth*2 - mainBorderWidth)
                //arrow left part
                mainBorderPath.lineTo(
                    viewWidth - (startPosition + arrowWidth - outerBorderWidth/4 - mainBorderWidth/2 ),
                    viewHeight - arrowHeight - outerBorderWidth/2 - mainBorderWidth/2
                )
                //bottom second part
                mainBorderPath.lineTo((outerBorderWidth + mainBorderWidth/2).toFloat(), viewHeight - arrowHeight - outerBorderWidth/2 - mainBorderWidth/2)
                //left line
                mainBorderPath.lineTo((outerBorderWidth + mainBorderWidth/2).toFloat(), (outerBorderWidth).toFloat())
                drawingCanvas.drawPath(mainBorderPath, mainBorderPaint)
            }
        }

        ///translate canvas to the child can be drawn now
        canvas?.save()
//        canvas?.translate((2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding), (2 * outerBorderWidth) + (2 * innerBorderWidth) + (2 * innerPadding))
        super.onDraw(canvas)
        canvas?.restore()
    }
}