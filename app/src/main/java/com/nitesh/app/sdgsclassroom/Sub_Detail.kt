package com.nitesh.app.sdgsclassroom

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.nitesh.app.sdgsclassroom.databinding.ActivitySubDetailBinding

class Sub_Detail : AppCompatActivity() {
    lateinit var binding: ActivitySubDetailBinding
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val goalId = intent.getStringExtra("goalId").toString()
        retrieveGoalImage(goalId)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnGoal.setOnClickListener {
            val intent = Intent(this, Goal_Detail::class.java)
            intent.putExtra("goalId", goalId)
            startActivity(intent)
        }

    }

    private fun retrieveGoalImage(goalId: String) {
        firestore.collection("goalsimage").document(goalId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("image_url")) {
                    val imageUrl = document.getString("image_url")
                    // Load image into ImageView using Glide
                    Glide.with(this)
                        .load(imageUrl)
                        .into(binding.goalbanner)
                    binding.progressBar.visibility = android.view.View.GONE
                } else {
                    Toast.makeText(this, "No image found for this goal", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load image!", Toast.LENGTH_SHORT).show()
            }
    }
}