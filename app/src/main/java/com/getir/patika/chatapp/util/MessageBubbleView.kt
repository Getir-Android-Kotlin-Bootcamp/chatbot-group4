package com.getir.patika.chatapp.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView

class MessageBubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val messageTextView: TextView

    init {

        messageTextView = TextView(context)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(20, 10, 20, 10) // Set margins for the text view
        messageTextView.layoutParams = params
        addView(messageTextView)
    }


    fun setMessage(message: String) {
        messageTextView.text = message

    }


    fun setTextColor(color: Int) {
        messageTextView.setTextColor(color)
    }


    fun setBubbleBackground(color: Int, floatArray: FloatArray) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArray

        shape.setColor(color)
        background = shape
    }
}
