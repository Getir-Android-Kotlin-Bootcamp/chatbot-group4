package com.getir.patika.chatapp.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.getir.patika.chatapp.R
import kotlinx.coroutines.*

/**
 * Custom view representing a dot progress bar.
 *
 * @param context The context in which the view is created.
 * @param attrs The attribute set used to inflate the view.
 * @param defStyleAttr The default style attribute.
 */
class DotProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentDotIndex = 0
    private val scope = CoroutineScope(Dispatchers.Main)
    private val animationOffset = 150L
    private var dotColor: Int = Color.BLACK
    private var dotSize: Int = context.resources.getDimensionPixelSize(R.dimen.default_dot_size)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DotProgressBar,
            0, 0
        ).apply {
            try {
                dotColor = getColor(R.styleable.DotProgressBar_dotColor, dotColor)
                dotSize = getDimensionPixelSize(R.styleable.DotProgressBar_dotSize, dotSize)
            } finally {
                recycle()
            }
        }

        /**
         * Inflates the dot progress bar layout and attaches it to this `DotProgressBar`.
         *
         * @param R.layout.dot_progress_bar The XML layout resource defining the dot views.
         * @param this The `DotProgressBar` instance serving as the parent for the inflated views.
         * @param true Indicates that the inflated views should be attached to the parent immediately.
         */
        LayoutInflater.from(context).inflate(R.layout.dot_progress_bar, this, true)

        orientation = HORIZONTAL
        gravity = CENTER

        for (i in 0 until childCount) {
            val dot = getChildAt(i)
            val layoutParams = dot.layoutParams as LayoutParams
            layoutParams.width = dotSize
            layoutParams.height = dotSize
            layoutParams.setMargins(6, 32, 6, 16)
            dot.layoutParams = layoutParams

            val drawable = ContextCompat.getDrawable(context, R.drawable.shape_dot)
                ?.mutate() as GradientDrawable
            drawable.setColor(dotColor)
            dot.background = drawable
        }

        startAnimation()
    }

    /**
     * Starts the animation for the dot progress bar.
     */
    private fun startAnimation() {
        scope.launch {
            val dots = children.toList()
            while (isActive) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.translate_wave)
                dots[currentDotIndex].startAnimation(anim)

                currentDotIndex = (currentDotIndex + 1) % dots.size

                delay(animationOffset)
            }
        }
    }

    /**
     * Stops the animation for the dot progress bar.
     */
    fun stopAnimation() {
        scope.cancel()
    }
}
