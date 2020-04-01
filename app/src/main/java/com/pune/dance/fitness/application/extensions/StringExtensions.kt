package com.pune.dance.fitness.application.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil

fun String.openLink(context: Context) {
    if (URLUtil.isValidUrl(this)) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(this)))
    }
}