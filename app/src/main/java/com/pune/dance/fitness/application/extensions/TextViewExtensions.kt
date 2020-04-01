package com.pune.dance.fitness.application.extensions

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("parseHtml")
fun TextView.parseHtml(htmlText: String?) {
    if (htmlText.isNullOrEmpty().not()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            this.text = Html.fromHtml(htmlText)
        }
    }
}