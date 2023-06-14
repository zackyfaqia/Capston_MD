package com.capstone.nempatin.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nempatin.R
import com.capstone.nempatin.databinding.ActivitySignupBinding
import com.capstone.nempatin.utils.FirebaseAuthHelper
import com.capstone.nempatin.utils.GoogleSignInHelper
import com.capstone.nempatin.utils.UiUpdater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authHelper: FirebaseAuthHelper
    private lateinit var uiUpdater: UiUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        authHelper = FirebaseAuthHelper(auth)
        uiUpdater = UiUpdater(this)

        val googleSignInHelper = GoogleSignInHelper(this, RC_SIGN_IN)
        binding.btnGoogleSignUp.setOnClickListener {
            googleSignInHelper.performSignIn()
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.etEmailInputSignup.text.toString()
            val password = binding.etPasswordInputSignup.text.toString()

            authHelper.signUpWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        uiUpdater.updateUI(user)
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        uiUpdater.updateUI(null)
                    }
                }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authHelper.firebaseAuthWithGoogle(account)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            uiUpdater.updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            uiUpdater.updateUI(null)
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
