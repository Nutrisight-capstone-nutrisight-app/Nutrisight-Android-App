package com.capstone.nutrisight.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.nutrisight.R


class CustomEditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }


    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {

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

    fun checkEditTextPassword(): Boolean {
        return if (text.toString().length < 8) {
            setError(context.getString(R.string.invalid_password), null)
            true
        } else {
            error = null
            false
        }
    }


}
