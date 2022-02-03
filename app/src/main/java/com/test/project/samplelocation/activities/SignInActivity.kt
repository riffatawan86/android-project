package com.test.project.samplelocation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.test.project.samplelocation.R
import com.test.project.samplelocation.utils.AppConstants
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private var mEmail: EditText? = null
    private var mPassword: EditText? = null
    private var mLogin: CardView? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }


    private fun initViews() {
        animationView.enableMergePathsForKitKatAndAbove(true)
        tvBottom2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        tvForgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        mAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                //Log.d("FBDFAS:", "id " + user.uid)
                Log.d("FBDFAS:", "auth id  " + mAuth!!.currentUser!!.uid)
                try {
                    val intent =
                        Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@AuthStateListener
            }
        }
        skipBtn.setOnClickListener {
            val intent =
                Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        mEmail = findViewById<View>(R.id.email) as EditText
        mPassword = findViewById<View>(R.id.password) as EditText
        mLogin = findViewById<View>(R.id.verificationBtn) as CardView
        mLogin!!.setOnClickListener {
            val email = mEmail!!.text.toString().trim()
            val password = mPassword!!.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this@SignInActivity,
                    "Please enter email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this@SignInActivity,
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    this@SignInActivity,
                    "Password too short, Should be greater than 6",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            try {

                progressBar.visibility = View.VISIBLE
                mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    this@SignInActivity
                ) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignInActivity,
                            "Sign in error",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    } else {
                        try {
                            val myPrefs =
                                getSharedPreferences(AppConstants.MY_PREFS, Context.MODE_PRIVATE)
                            myPrefs.edit().putString(AppConstants.USER_EMAIL, email).apply()
                            val intent =
                                Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        progressBar.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(firebaseAuthListener!!)
    }
}
