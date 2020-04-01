package com.pune.dance.fitness.application.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Int.dpToPixels(context: Context): Int {
    return this.toFloat().dpToPixels(context).toInt()
}

fun Float.dpToPixels(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun Int.getColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

fun Int.getFont(context: Context): Typeface? {
    return ResourcesCompat.getFont(context, this)
}

fun Int.getDrawable(context: Context): Drawable? {
    return AppCompatResources.getDrawable(context, this)
}

fun Int.getColorStateList(context: Context): ColorStateList? {
    return ContextCompat.getColorStateList(context, this)
}

fun Int.getTwoDigitString(): String {
    if (this < 10) {
        return "0$this"
    }
    return "$this"
}

/*inline fun <reified T : ViewDataBinding> Int.createBinding(context: Context, parent: ViewGroup): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), this, parent, false)
}

// requires Int in millis
fun Int.formattedTimeString(): String {
    return Calendar.getInstance().let {
        it.time = it.time.stripTime()
        it.set(Calendar.MILLISECOND, this)
        return@let it.getFormattedString()
    }
}*/

fun Int.getDaySuffix(): String {
    if (this in 11..13) {
        return "th"
    }
    return when (this % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}
