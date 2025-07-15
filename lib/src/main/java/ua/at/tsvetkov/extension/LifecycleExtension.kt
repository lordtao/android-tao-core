package ua.at.tsvetkov.extension

import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Fragment.launchWhenStarted(
	block: suspend CoroutineScope.() -> Unit,
): Job {
	return lifecycleScope.launch {
		viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			block.invoke(this)
		}
	}
}

fun ComponentActivity.launchWhenStarted(
	block: suspend CoroutineScope.() -> Unit,
): Job {
	return lifecycleScope.launch {
		repeatOnLifecycle(Lifecycle.State.STARTED) {
			block.invoke(this)
		}
	}
}

fun ComponentActivity.launchWhenCreated(
	block: suspend CoroutineScope.() -> Unit,
): Job {
	return lifecycleScope.launch {
		repeatOnLifecycle(Lifecycle.State.CREATED) {
			block.invoke(this)
		}
	}
}
