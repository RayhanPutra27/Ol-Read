package rpl.ezy.olread.adapter

import android.content.Context
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var enabled: Boolean? = false

    init {
        this.enabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled!!) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled!!) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

}