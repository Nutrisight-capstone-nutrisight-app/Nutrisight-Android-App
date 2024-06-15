package com.capstone.nutrisight.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.nutrisight.R


class CustomEditTextEmpty @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    fun checkEditTextEmpty(): Boolean {
        return if (text.isNullOrEmpty()) {
            setError(context.resources.getString(R.string.must_fill), null)
            true
        } else {
            error = null
            false
        }
    }


}
