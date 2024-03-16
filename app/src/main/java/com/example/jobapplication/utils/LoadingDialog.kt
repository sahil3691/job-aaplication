package com.example.jobapplication.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.example.jobapplication.R


object LoadingDialog {
    private var dialog: Dialog? = null

    fun showDialog(context: Context) {
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.loading, null)
        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.loading_animation)

        dialog = Dialog(context, R.style.LoadingDialogTheme)
        dialog?.setContentView(dialogView)
        dialog?.setCancelable(false)

        animationView.setAnimation(R.raw.loading) // Replace with your Lottie animation file
        animationView.playAnimation()

        dialog?.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
        dialog = null
    }
}
