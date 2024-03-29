package com.enecuumwallet.androidapp.utils

import android.widget.ImageView
import com.enecuumwallet.androidapp.R

/**
 * Created by oleg on 26.01.18.
 */
object PinUtils {

    fun changePinState(pin1: ImageView, pin2: ImageView, pin3: ImageView, pin4: ImageView, currentLength : Int) {
        changeDotState(currentLength, 0, pin1)
        changeDotState(currentLength, 1, pin2)
        changeDotState(currentLength, 2, pin3)
        changeDotState(currentLength, 3, pin4)
    }

    private fun changeDotState(length: Int, value2check : Int, dot: ImageView) {
        if(length > value2check)
            dot.setImageResource(R.drawable.dot_1)
        else
            dot.setImageResource(R.drawable.dot_2)
    }
}