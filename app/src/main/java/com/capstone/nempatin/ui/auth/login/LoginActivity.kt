package com.capstone.nempatin.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nempatin.data.response.login.FirebaseAuthSource
import com.capstone.nempatin.databinding.ActivityLoginBinding
import com.capstone.nempatin.domain.UserManager
import com.capstone.nempatin.ui.auth.register.SignUpActivity
import com.capstone.nempatin.ui.main.MainActivity
import com.capstone.nempatin.utils.GoogleSignInHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authSource = FirebaseAuthSource()
        userManager = UserManager(authSource)

        binding.btnGoogleSignIn.setOnClickListener {
            val googleSignInHelper = GoogleSignInHelper(this, RC_SIGN_IN)
            googleSignInHelper.performSignIn()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailInput.text.toString()
            val password = binding.etPasswordInput.text.toString()

            userManager.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = userManager.getCurrentUser()
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                userManager.signInWithGoogle(account)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = userManager.getCurrentUser()
                            updateUI(user)
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Show error message if needed
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
