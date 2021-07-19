package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (old) {
            ButtonState.Clicked -> {
                if (old == ButtonState.Loading) return@observable
                createClickAnimation()
            }
            ButtonState.Loading -> createLoadingAnimation()
            ButtonState.Completed -> createCompletedAnimation()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var text = resources.getString(R.string.button_name)
    private var textSize = resources.getDimension(R.dimen.default_text_size)
    private var textColor = resources.getColor(R.color.white, context.theme)
    private var buttonColor = resources.getColor(R.color.colorPrimary, context.theme)

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textSize = getFloat(R.styleable.LoadingButton_android_textSize, textSize)
            textColor = getColor(R.styleable.LoadingButton_android_textColor, textColor)
            buttonColor = getColor(R.styleable.LoadingButton_android_textColor, buttonColor)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        //

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            setBackgroundColor(buttonColor)
        }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val width: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val height: Int = resolveSizeAndState(
            MeasureSpec.getSize(width),
            heightMeasureSpec,
            0
        )
        widthSize = width
        heightSize = height
        setMeasuredDimension(width, height)
    }

    private fun createClickAnimation() {

    }

    private fun createLoadingAnimation() {

    }

    private fun createCompletedAnimation() {

    }
}