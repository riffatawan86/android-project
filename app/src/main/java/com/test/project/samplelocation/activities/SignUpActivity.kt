package  com.test.project.samplelocation.activities

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.test.project.samplelocation.R
import com.test.project.samplelocation.models.UserModel
import com.test.project.samplelocation.utils.AppConstants
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private var mEmail: EditText? = null
    private var mPassword: EditText? = null
    private var mContact: EditText? = null
    private var mName: EditText? = null
    private var mSignUp: CardView? = null
    private var mAuth: FirebaseAuth? = null
    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }


    private fun initViews() {
        animationView.enableMergePathsForKitKatAndAbove(true)
        dbReference = FirebaseDatabase.getInstance().getReference("Users")
        tvBottom2.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        mEmail = findViewById<View>(R.id.email) as EditText
        mPassword = findViewById<View>(R.id.password) as EditText
        mContact = findViewById<View>(R.id.contact) as EditText
        mName = findViewById<View>(R.id.name) as EditText
        mSignUp = findViewById<View>(R.id.signUp) as CardView
        mSignUp!!.setOnClickListener {
            val email = mEmail!!.text.toString().trim()
            val password = mPassword!!.text.toString().trim()
            val contact = mContact!!.text.toString().trim()
            val name = mName!!.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter your name",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(contact)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter contact",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Password too short, Should be greater than 6",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (contact.length < 11) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Contact shouldn't be less than 11 digit",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!contact.startsWith("03", true)) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please Enter valid contact number!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!mEmail!!.text.toString().trim { it <= ' ' }
                    .contains("@") || !mEmail!!.text.toString().trim { it <= ' ' }.contains(
                    ".com"
                )
            ) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter valid  email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            try {
                progressBar.visibility = View.VISIBLE
                mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this@SignUpActivity
                ) { task ->
                    if (!task.isSuccessful) {
                        try {
                            Toast.makeText(
                                this@SignUpActivity,
                                "${task.exception!!.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                        }
                        Log.d("CHECKER", " " + task.exception!!.localizedMessage)
                        progressBar.visibility = View.GONE
                    } else {
                        val userId = mAuth!!.getCurrentUser()!!.uid
                        val model =
                            UserModel(
                                name,
                                email,
                                contact
                            )
                        dbReference.child(userId).setValue(model)
                        val myPrefs =
                            getSharedPreferences(AppConstants.MY_PREFS, Context.MODE_PRIVATE)
                        myPrefs.edit().putString(AppConstants.USER_ID, userId).apply()
                        myPrefs.edit().putString(AppConstants.USER_EMAIL, email).apply()
                        myPrefs.edit().putString(AppConstants.USER_CONTACT, contact).apply()
                        myPrefs.edit().putString(AppConstants.USER_NAME, name).apply()
                        progressBar.visibility = View.GONE
                        try {
                            val intent =
                                Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
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
