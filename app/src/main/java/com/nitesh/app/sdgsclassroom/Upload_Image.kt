package com.nitesh.app.sdgsclassroom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nitesh.app.sdgsclassroom.databinding.ActivityUploadImageBinding
import java.util.UUID

class Upload_Image : AppCompatActivity() {
    private lateinit var binding: ActivityUploadImageBinding
    private lateinit var imageUri: Uri
    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1000)
        }
        binding.buttonUploadImage.setOnClickListener {
            val goalId = binding.goalid.text.toString().trim()

            if (goalId.isEmpty()) {
                Toast.makeText(this, "Please enter a valid Goal ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadImageToFirebase(goalId)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.goalImageView.setImageURI(imageUri)  // Display selected image
        }
    }

    private fun uploadImageToFirebase(goalId: String) {
        if (!::imageUri.isInitialized) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }
        val imagePath = "goal_images/$goalId/${UUID.randomUUID()}.jpg"
        val storageRef = storage.reference.child(imagePath)

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val goalData = hashMapOf("image_url" to downloadUrl.toString())
                firestore.collection("goalsimage").document(goalId).set(goalData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save image URL!", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image upload failed!", Toast.LENGTH_SHORT).show()
        }
    }
}