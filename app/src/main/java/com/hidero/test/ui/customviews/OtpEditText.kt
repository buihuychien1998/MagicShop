package com.hidero.test.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.hidero.test.R

class OtpEditText : AppCompatEditText {
    var mMaxLength = 6
    private var mSpace = 10f //24 dp by default, space between the lines
    private var mNumChars = 6f
    private var mLineSpacing = 4f //8dp by default, height of the text from our lines
    private var mLineStroke = 2f
    private var mLinesPaint: Paint? = null
    private var mClickListener: OnClickListener? = null
    private var canvas: Canvas? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    var maxLength: Int
        get() = mMaxLength
        set(maxLength) {
            mMaxLength = maxLength
            mNumChars = mMaxLength.toFloat()
            if (canvas != null) drawMethod(canvas!!)
        }

    private fun init(context: Context, attrs: AttributeSet) {
        val multi: Float = context.resources.displayMetrics.density
        mLineStroke *= multi
        mLinesPaint = Paint(paint).apply {
            strokeWidth = mLineStroke
            color = ContextCompat.getColor(context, R.color.white)
        }
        setBackgroundResource(0)
        mSpace *= multi //convert to pixels for our density
        mLineSpacing *= multi //convert to pixels for our density
        mNumChars = mMaxLength.toFloat()
        super.setOnClickListener { v -> // When tapped, move cursor to end of text.
            setSelection(text!!.length)
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback?) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        drawMethod(canvas)
    }

    private fun drawMethod(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        val mCharSize: Float
        mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }
        var startX = paddingLeft.toFloat()
        val bottom = (height - paddingBottom).toFloat()

        //Text Width
        val text = text
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(getText(), 0, textLength, textWidths)
        paint.color = Color.parseColor("#FFFFFF") // Here we are set text color.
        var i = 0
        while (i < mNumChars) {
            canvas.drawLine(startX, bottom, startX + mCharSize, bottom, mLinesPaint!!)
            if (getText()!!.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(
                    text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing, paint
                )
            }
            if (mSpace < 0) {
                startX += mCharSize * 2
            } else {
                startX += mCharSize + mSpace
            }
            i++
        }
    }
}