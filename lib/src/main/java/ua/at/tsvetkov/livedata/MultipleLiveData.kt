package ua.at.tsvetkov.mvi

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Alexandr Tsvetkov on 07.09.2022.
 *
 * Added to MutableLiveData ability for passing any count of data any time.
 * MutableLiveData docs - "If you called this method (postValue)
 * multiple times before a main thread executed a posted task, only the last value would be dispatched."
 */
open class MultipleValuesLiveData<T> : MutableLiveData<T>() {

    private val isPending = AtomicBoolean(false)
    private val values: Queue<T> = LinkedList()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            throw IllegalAccessException("Multiple observers registered, but we can use only one.")
        }
        super.observe(owner) { t: T? ->
            if (t == null) return@observe
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(t)
                if (values.isNotEmpty()) {
                    pollValue()
                }
            }
        }
    }

    override fun postValue(value: T) {
        values.add(value)
        pollValue()
    }

    @MainThread
    override fun setValue(t: T?) {
        isPending.set(true)
        super.setValue(t)
    }

    private fun pollValue() {
        CoroutineScope(Dispatchers.Main).launch {
            if (values.size > 0) {
                value = values.poll()
            }
        }
    }
}
