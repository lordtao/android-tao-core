package ua.at.tsvetkov.extension

import android.view.View

/**
 * Created by Alexandr Tsvetkov on 20.11.2022.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}