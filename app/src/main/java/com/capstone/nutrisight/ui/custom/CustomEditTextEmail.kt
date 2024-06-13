package com.capstone.nutrisight.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class CustomEditTextEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs), View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!text.isNullOrEmpty() && !text.matches(emailPattern.toRegex())) {
            setError("Invalid email format", null)
        } else {
            error = null
        }
    }

    fun checkEditTextEmpty(): Boolean {
        return if (text.isNullOrEmpty()) {
            setError("You must fill this field", null)
            true
        } else {
            error = null
            false
        }
    }
}