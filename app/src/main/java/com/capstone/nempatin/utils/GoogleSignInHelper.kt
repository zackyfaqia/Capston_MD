package com.capstone.nempatin.utils

import android.app.Activity
import android.content.Intent
import com.capstone.nempatin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInHelper(private val activity: Activity, private val requestCode: Int) {

    fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        return googleSignInClient.signInIntent
    }

    fun performSignIn() {
        val signInIntent = getSignInIntent()
        activity.startActivityForResult(signInIntent, requestCode)
    }
}
