package ua.at.tsvetkov.bindings

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import ua.at.tsvetkov.util.logger.Log
import kotlin.let

@BindingAdapter("setDrawableResource")
fun ImageView.setDrawableResource(drawableId: Int?) {
    try {
        drawableId?.let { this.setImageResource(it) }
    } catch (e: Exception) {
        Log.e(e)
    }
}

@BindingAdapter("tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}

@BindingAdapter("imageUri")
fun ImageView.setImageUri(uriStr: String?) {
    this.setImageURI(uriStr?.toUri())
}

@BindingAdapter("imageUri")
fun ImageView.setImageUri(uri: Uri?) {
    this.setImageURI(uri)
}