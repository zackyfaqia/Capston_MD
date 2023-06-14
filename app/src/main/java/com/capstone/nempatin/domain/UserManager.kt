package com.capstone.nempatin.domain

import com.capstone.nempatin.data.response.login.FirebaseAuthSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class UserManager(private val authSource: FirebaseAuthSource) {

    fun signInWithGoogle(account: GoogleSignInAccount): Task<AuthResult> {
        return authSource.signInWithGoogle(account)
    }

    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return authSource.signInWithEmailAndPassword(email, password)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authSource.getCurrentUser()
    }
}
