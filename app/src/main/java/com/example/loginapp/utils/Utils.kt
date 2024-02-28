package com.example.loginapp.utils

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

class Utils {


    companion object HideAppBar {

        private var progressDialog: WeakReference<ProgressDialog>? = null
        fun hideAppBar(activity: AppCompatActivity) {
            activity.supportActionBar!!.hide()
        }

        fun showProgress(activity: AppCompatActivity) {
            dismissProgress()
            val progress = ProgressDialog(activity)
            progress.setMessage("Loading...")
            progress.setCancelable(false)
            progressDialog = WeakReference(progress)

            progress.show()
        }

        fun dismissProgress() {
            progressDialog?.get()?.dismiss()
            progressDialog = null
        }
    }
}