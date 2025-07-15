package com.example.kutlaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kutlaapp.R
import com.example.kutlaapp.ui.main.MainActivity
import com.example.kutlaapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    // Google Sign-In sonucu yakalayan ActivityResultLauncher
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        Log.d("GoogleSignIn", "Google Sign-In sonucu alındı. resultCode: ${result.resultCode}")

        if (result.resultCode == RESULT_OK) {
            val data = result.data
            Log.d("GoogleSignIn", "Intent data: $data")

            if (data != null) {
                for (key in data.extras?.keySet() ?: emptySet()) {
                    Log.d("GoogleSignIn", "Intent Extra: $key -> ${data.extras?.get(key)}")
                }
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if (account != null) {
                    Log.d("GoogleSignIn", "Google hesabı alındı: ${account.email}, ID Token: ${account.idToken}")
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    Log.e("GoogleSignIn", "Hesap bilgileri alınamadı")
                    Toast.makeText(this, "Hesap bilgileri alınamadı", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google giriş hatası! Code: ${e.statusCode}, Message: ${e.message}")
                Toast.makeText(this, "Google giriş hatası: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("GoogleSignIn", "Google Sign-In başarısız! resultCode: ${result.resultCode}")
            Toast.makeText(this, "Google giriş başarısız!", Toast.LENGTH_SHORT).show()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            loginWithEmailPassword(email, password)
        }

        binding.btnGoogleSignin.setOnClickListener {
            signInWithGoogle()
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Log.d("Auth", "Kullanıcı zaten giriş yapmış: ${auth.currentUser?.email}")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loginWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Auth", "Email/Şifre ile giriş başarılı: ${user?.email}")
                    Toast.makeText(this, "Giriş başarılı: ${user?.email}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("Auth", "Email/Şifre ile giriş başarısız: ${task.exception?.message}")
                    Toast.makeText(this, "Giriş başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // E-posta bilgisi al
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d("GoogleSignIn", "Firebase Authentication'a Google token ile giriş yapılıyor...")
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("GoogleSignIn", "Google ile giriş başarılı: ${user?.email}")
                    Toast.makeText(this, "Google ile giriş başarılı: ${user?.email}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("GoogleSignIn", "Google giriş başarısız: ${task.exception?.message}")
                    Toast.makeText(this, "Google giriş başarısız", Toast.LENGTH_SHORT).show()
                }
            }
    }
}