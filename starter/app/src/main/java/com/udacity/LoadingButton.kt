package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var loadingAnimator = ValueAnimator()
    private var downloaded = false

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> onClick()
            ButtonState.Loading -> onLoading()
            ButtonState.Completed -> onComplete()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var textRect = Rect()
    private var arcRect = RectF()
    private var progressValue: Float = 0f
    private var text = resources.getString(R.string.button_download_text)

    private var textColor = resources.getColor(R.color.white, context.theme)
    private var buttonColor = resources.getColor(R.color.colorPrimary, context.theme)
    private var arcLoadingColor = resources.getColor(R.color.colorAccent, context.theme)
    private var buttonLoadingColor = resources.getColor(R.color.colorPrimaryDark, context.theme)

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textColor = getColor(R.styleable.LoadingButton_android_textColor, textColor)
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, buttonColor)
            arcLoadingColor = getColor(R.styleable.LoadingButton_arcLoadingColor, arcLoadingColor)
            buttonLoadingColor =
                getColor(R.styleable.LoadingButton_buttonLoadingColor, buttonLoadingColor)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            setBackgroundColor(buttonColor)

            if (buttonState == ButtonState.Loading) {
                drawLoadingRect(it)
                drawLoadingCircle(it)
            }
            drawText(it)
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

        arcRect.apply {
            top = 15f
            bottom = heightSize - 15f
            left = widthSize - bottom
            right = widthSize - 15f
        }
    }

    private fun drawLoadingRect(canvas: Canvas) {
        paint.color = buttonLoadingColor
        canvas.drawRect(0f, 0f, progressValue / 100f * widthSize, heightSize.toFloat(), paint)
    }

    private fun drawLoadingCircle(canvas: Canvas) {
        paint.color = arcLoadingColor
        canvas.drawArc(arcRect, 0f, progressValue / 100f * 360f, true, paint)
    }

    private fun drawText(canvas: Canvas) {
        paint.color = textColor
        paint.getTextBounds(text, 0, text.length, textRect)
        val centerPosition = measuredHeight.toFloat() / 2 - textRect.centerY()
        canvas.drawText(text, widthSize / 2f, centerPosition, paint)
    }

    private fun onClick() {
        isEnabled = false
        buttonState = ButtonState.Loading
    }

    private fun onLoading() {
        loadingAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
            addUpdateListener {
                progressValue = animatedValue as Float
                if (progressValue >= 99f && !downloaded) pause()
                invalidate()
            }
            duration = 15000
            interpolator = DecelerateInterpolator(1.2f)
            doOnStart {
                text = resources.getString(R.string.button_download_loading_text)
            }
            start()
        }
    }

    private fun onComplete() {
        isEnabled = true
        text = resources.getString(R.string.button_download_text)
    }

    fun buttonClicked() {
        downloaded = false
        buttonState = ButtonState.Clicked
    }

    fun downloadComplete() {
        downloaded = true
        progressValue = 0f
        val animatedFraction = loadingAnimator.animatedFraction
        loadingAnimator.apply {
            if (isPaused) {
                resume()
            } else {
                cancel()
                setCurrentFraction(animatedFraction)
                duration = 1000
                start()
            }
            doOnEnd {
                buttonState = ButtonState.Completed
            }
        }
    }
}