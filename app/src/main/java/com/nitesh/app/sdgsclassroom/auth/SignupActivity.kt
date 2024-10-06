package com.nitesh.app.sdgsclassroom.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nitesh.app.sdgsclassroom.R
import com.nitesh.app.sdgsclassroom.databinding.ActivitySignupBinding
import java.util.UUID


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        dbref = FirebaseDatabase.getInstance().reference
        binding.btnSignUp.setOnClickListener {
            val name = binding.SignUpName.text.toString()
            val email = binding.SignUpemailTv.text.toString()
            val pass = binding.SignUppassTv.text.toString()
            signup(name,email,pass)
        }

        binding.tvHaveAcc.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun signup(name: String, email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    StoreUserData(name,email,pass,mAuth.currentUser?.uid)
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name) // Set the user's display name
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            Toast.makeText(this, "User profile updated with name.", Toast.LENGTH_SHORT).show()
                            // Save the name in the database if needed
                        }
                    }
                    Toast.makeText(this,"SignUp Succesfully!Login Now...", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this,"Error Occured: ${task.exception!!.message.toString()}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun StoreUserData(name: String, email: String, pass: String, uid: String?) {
        dbref.child("Users").child(uid!!).setValue(User(name,email,pass,uid!!)).addOnFailureListener {
            Toast.makeText(this,it.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}