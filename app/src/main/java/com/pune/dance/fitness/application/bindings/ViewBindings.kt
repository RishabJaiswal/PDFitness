package com.pune.dance.fitness.application.bindings

import android.view.View
import androidx.databinding.BindingAdapter
import com.pune.dance.fitness.application.extensions.visibleOrGone
import com.pune.dance.fitness.application.extensions.visibleOrInvisible

@BindingAdapter("visibleOrGone")
fun setVisibleOrGone(view: View, visible: Boolean) {
    view.visibleOrGone(visible)
}

@BindingAdapter("visibleOrInvisible")
fun setVisibleOrInvisible(view: View, visible: Boolean) {
    view.visibleOrInvisible(visible)
}