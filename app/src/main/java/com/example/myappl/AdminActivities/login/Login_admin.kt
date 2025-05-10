package com.example.myappl.AdminActivities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myappl.AdminActivities.AdminPage
import com.example.myappl.MainActivity
import com.example.myappl.R
import com.example.myappl.SignupActivity
import com.example.myappl.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login_admin : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_admin)
        var loginEmail = findViewById<EditText>(R.id.login_emaill)
        var loginPassword = findViewById<EditText>(R.id.login_passwordd)
        var loginButton = findViewById<Button>(R.id.login_buttonn)
        var signupRedirectText = findViewById<TextView>(R.id.signupRedirectTextt)
        firebaseAuth = FirebaseAuth.getInstance()
        signupRedirectText.setOnClickListener {
            val Intent = Intent(this, SignupActivity::class.java)
            startActivity(Intent)
        }

            loginButton.setOnClickListener {
                val email = loginEmail.text.toString()
                val password = loginPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this, AdminPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }

    }
}