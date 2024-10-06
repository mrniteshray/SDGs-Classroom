package com.nitesh.app.sdgsclassroom.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.nitesh.app.sdgsclassroom.MainActivity
import com.nitesh.app.sdgsclassroom.R
import com.nitesh.app.sdgsclassroom.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        binding.Login.setOnClickListener {
            val email = binding.emailTv.text.toString()
            val pass = binding.passTv.text.toString()
            login(email,pass)
        }

        binding.tvNotHaveAcc.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }
    }

    private fun login(email:String,pass:String){
        if(email.isEmpty() && pass.isEmpty()){
            Toast.makeText(this,"Feilds are empty!!", Toast.LENGTH_LONG).show()
        }else{
            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Login Successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this,"User doesn't exist", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}