package com.capstone.nempatin.utils

import android.content.Intent
import com.capstone.nempatin.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import androidx.appcompat.app.AppCompatActivity

class UiUpdater(private val activity: AppCompatActivity) {

    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed up/in, navigate to the MainActivity
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            // If you want to remove SignUpActivity/LoginActivity from the backstack after the user has signed in, you can call finish()
            activity.finish()
        } else {
            // User is not signed up/in, stay on SignUpActivity/LoginActivity and maybe show an error message
        }
    }
}
