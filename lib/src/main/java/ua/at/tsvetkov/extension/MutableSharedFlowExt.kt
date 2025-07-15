package com.dsi.cochlea.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Created by Alexandr Tsvetkov on 12.02.2025.
 */

fun <T> MutableSharedFlow<T>.emitOn(dispatcher: CoroutineDispatcher, value: T) {
    CoroutineScope(dispatcher).launch {
        emit(value)
    }
}

fun <T> MutableSharedFlow<T>.emitOnIO(value: T) {
    CoroutineScope(Dispatchers.IO).launch {
        emit(value)
    }
}

fun <T> MutableSharedFlow<T>.emitOnMain(value: T) {
    CoroutineScope(Dispatchers.Main).launch {
        emit(value)
    }
}