package com.pmb.traveljournal.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.pmb.traveljournal.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var googleAuth: Button
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login) // Use your Login Activity layout XML

        // Inisialisasi elemen UI
        googleAuth = findViewById(R.id.btnLoginGoogle)
        loginButton = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance()

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set OnClickListener Google Sign In
        googleAuth.setOnClickListener {
            googleSignIn()
        }

        // Set OnClickListener untuk Login Email/Password
        val onClickListener = loginButton.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginWithEmail(email, password)
            } else {
                MotionToast.createToast(
                    this,
                    "Invalid Input",
                    "Please enter both email and password",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }

        // Konfigurasi ClickableSpan untuk "Register"
        setClickableRegisterText()
    }

    private fun setClickableRegisterText() {
        val fullText = getString(R.string.new_to_horizon_register) // "New to Horizon? Register"
        val spannableString = SpannableString(fullText)

        // Cari posisi kata "Register"
        val startIndex = fullText.indexOf("Register")
        val endIndex = startIndex + "Register".length

        // Tambahkan ClickableSpan hanya pada "Register"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navigasi ke RegisterActivity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        // Tambahkan warna teks (opsional)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.black, theme)),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set text ke TextView
        tvRegister.text = spannableString
        tvRegister.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it.idToken!!)
                }
            } catch (e: ApiException) {
                MotionToast.createToast(
                    this,
                    "Google Sign-In Failed",
                    e.localizedMessage ?: "Unknown error occurred",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserToDatabase(user)
                    // Arahkan ke DashboardActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(user: FirebaseUser?) {
        user?.let {
            val map = HashMap<String, String>()
            map["email"] = it.email ?: "N/A"
            map["uid"] = it.uid
            map["name"] = it.displayName ?: "N/A"
            map["phone"] = it.phoneNumber ?: "N/A"
            map["profileImage"] = it.photoUrl?.toString() ?: "N/A"

            database.reference.child("users").child(it.uid).setValue(map)
                .addOnSuccessListener {
                    // Ganti Toast dengan Motion Toast
                    MotionToast.createToast(
                        this,
                        "Success",
                        "User saved successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
                .addOnFailureListener { e ->
                    // Ganti Toast dengan Motion Toast
                    MotionToast.createToast(
                        this,
                        "Failed",
                        "Failed to save user: ${e.localizedMessage}",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
        }
    }
    private fun loginWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login berhasil, arahkan ke Dashboard
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    MotionToast.createToast(
                        this,
                        "Login Failed",
                        task.exception?.localizedMessage ?: "Authentication failed",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
            .addOnFailureListener { exception ->
                MotionToast.createToast(
                    this,
                    "Error",
                    exception.localizedMessage ?: "Unknown error occurred",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null
                )
            }
    }
}
