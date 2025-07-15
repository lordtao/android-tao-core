package com.dsi.cochlea.bindings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import ua.at.tsvetkov.util.logger.Log

/**
 * Created by Alexandr Tsvetkov on 03.10.2022.
 */

@BindingAdapter("isVisible")
fun View.setIsVisible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("isVisible")
fun View.isVisible(value: Any?) {
    isVisible = value != null
}

@BindingAdapter("isGone")
fun View.isGone(value: Any?) {
    isGone = value != null
}

@BindingAdapter("isGone")
fun View.setIsGone(value: Boolean) {
    isGone = value
}

fun View.gone() {
    isGone = true
}

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

@BindingAdapter("setColorResource")
fun View.setColorResource(colorId: Int?) {
    try {
        colorId?.let { this.setBackgroundColor(ContextCompat.getColor(context, it)) }
    } catch (e: kotlin.Exception) {
        Log.e(e)
    }
}