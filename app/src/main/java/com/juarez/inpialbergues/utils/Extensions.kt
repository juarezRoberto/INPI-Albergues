package com.juarez.inpialbergues.utils

import android.content.pm.PackageManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun ImageView.loadPhoto(url: String) {
    Glide
        .with(this.context)
        .load(url)
        .into(this)
}

fun Fragment.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), text, length).show()
}

enum class PermissionResult {
    GRANTED, DENIED, RATIONALE
}

fun Fragment.requestPermission(
    permission: String,
    onResult: (result: PermissionResult) -> Unit,
) {
    when {
        ContextCompat.checkSelfPermission(requireContext(),
            permission) == PackageManager.PERMISSION_GRANTED -> onResult(PermissionResult.GRANTED)
        shouldShowRequestPermissionRationale(permission) -> onResult(PermissionResult.RATIONALE)
        else -> onResult(PermissionResult.DENIED)
    }
}