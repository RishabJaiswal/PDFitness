package com.pune.dance.fitness.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    fun toast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @LayoutRes
    abstract fun getLayout(): Int
}