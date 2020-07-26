package com.wisnitech.marvelpaging.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}