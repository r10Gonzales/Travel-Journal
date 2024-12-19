package com.pmb.traveljournal.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.pmb.traveljournal.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Auth dan Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Inisialisasi elemen UI
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)
        tvLogin = findViewById(R.id.tvLogin)

        // Atur teks "Login" bisa diklik
        setClickableLoginText()

        // Set listener tombol register
        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInput(fullName, email, password, confirmPassword)) {
                progressBar.visibility = ProgressBar.VISIBLE
                registerUser(fullName, email, password)
            }
        }
    }

    private fun validateInput(fullName: String, email: String, password: String, confirmPassword: String): Boolean {
        when {
            TextUtils.isEmpty(fullName) -> {
                etFullName.error = "Nama tidak boleh kosong"
                return false
            }
            TextUtils.isEmpty(email) -> {
                etEmail.error = "Email tidak boleh kosong"
                return false
            }
            TextUtils.isEmpty(password) -> {
                etPassword.error = "Password tidak boleh kosong"
                return false
            }
            password.length < 6 -> {
                etPassword.error = "Password harus minimal 6 karakter"
                return false
            }
            password != confirmPassword -> {
                etConfirmPassword.error = "Konfirmasi password tidak sesuai"
                return false
            }
            else -> return true
        }
    }

    private fun registerUser(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registrasi berhasil, simpan data pengguna di database
                    val user = auth.currentUser
                    saveUserToDatabase(user, fullName)
                } else {
                    progressBar.visibility = ProgressBar.INVISIBLE
                    MotionToast.createToast(
                        this,
                        "Registrasi Gagal",
                        task.exception?.localizedMessage ?: "Terjadi kesalahan",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
            }
    }

    private fun saveUserToDatabase(user: FirebaseUser?, fullName: String) {
        user?.let {
            val userMap = hashMapOf(
                "uid" to it.uid,
                "name" to fullName,
                "email" to it.email,
                "profileImage" to "N/A"
            )

            database.reference.child("users").child(it.uid).setValue(userMap)
                .addOnSuccessListener {
                    progressBar.visibility = ProgressBar.INVISIBLE
                    MotionToast.createToast(
                        this,
                        "Registrasi Berhasil",
                        "Akun berhasil dibuat!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                    // Arahkan ke LoginActivity
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    progressBar.visibility = ProgressBar.INVISIBLE
                    MotionToast.createToast(
                        this,
                        "Gagal Menyimpan Data",
                        e.localizedMessage ?: "Terjadi kesalahan",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null
                    )
                }
        }
    }

    private fun setClickableLoginText() {
        val fullText = "Already have an account? Login"
        val spannableString = SpannableString(fullText)

        // Cari posisi kata "Login" dan jadikan clickable
        val startIndex = fullText.indexOf("Login")
        val endIndex = startIndex + "Login".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvLogin.text = spannableString
        tvLogin.movementMethod = LinkMovementMethod.getInstance()
    }
}
