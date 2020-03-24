package com.pune.dance.fitness.application

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.pune.dance.fitness.R
import com.pune.dance.fitness.application.extensions.setBackgrountTint

abstract class BaseActivity : AppCompatActivity() {

    private val inputService: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun toast(@StringRes message: Int) {
        createToast(getString(message)).show()
    }

    fun toast(message: String) {
        createToast(message).show()
    }

    fun createToast(message: String): Toast {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
            this.view.setBackgrountTint(R.color.squash)
        }
    }

    fun showKeyboard(view: View) {
        inputService.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(view: View) {
        inputService.hideSoftInputFromWindow(view.windowToken, 0)
    }
}