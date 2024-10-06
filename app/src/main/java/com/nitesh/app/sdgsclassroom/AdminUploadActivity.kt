package com.nitesh.app.sdgsclassroom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nitesh.app.sdgsclassroom.databinding.ActivityAdminUploadBinding

import java.util.*

class AdminUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUploadBinding
    private lateinit var infographicUriList: MutableList<Uri>
    private lateinit var audioUri: Uri

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infographicUriList = mutableListOf()

        binding.buttonUploadImage.setOnClickListener {
            selectImages()
        }

        binding.buttonUploadAudio.setOnClickListener {
            selectAudio()
        }

        binding.buttonSubmit.setOnClickListener {
            uploadData()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewInfographics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewInfographics.adapter = AdminInfographicAdapter(infographicUriList)
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Allow multiple selection
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    private fun selectAudio() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, AUDIO_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                IMAGE_REQUEST_CODE -> {
                    if (data.clipData != null) { // Multiple images selected
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val uri = data.clipData!!.getItemAt(i).uri
                            infographicUriList.add(uri)
                        }
                        binding.recyclerViewInfographics.adapter?.notifyDataSetChanged()
                        binding.recyclerViewInfographics.visibility = RecyclerView.VISIBLE
                    } else if (data.data != null) { // Single image selected
                        infographicUriList.add(data.data!!)
                        binding.recyclerViewInfographics.adapter?.notifyDataSetChanged()
                        binding.recyclerViewInfographics.visibility = RecyclerView.VISIBLE
                    }
                }
                AUDIO_REQUEST_CODE -> {
                    audioUri = data.data!!
                    binding.textViewAudioFile.text = "Audio file selected: ${audioUri.lastPathSegment}"
                    binding.textViewAudioFile.visibility = TextView.VISIBLE
                }
            }
        }
    }

    private fun uploadData() {
        val goalName = binding.editTextGoalName.text.toString()
        val goalDescription = binding.editTextGoalDescription.text.toString()
        val goalId = binding.editTextGoalId.text.toString()

        if (goalId.isEmpty() || goalName.isEmpty() || goalDescription.isEmpty() || infographicUriList.isEmpty() || !::audioUri.isInitialized) {
            Toast.makeText(this, "Please fill all fields and select files", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload audio to Firebase Storage
        val audioPath = "audio/${UUID.randomUUID()}.mp3"
        storage.reference.child(audioPath).putFile(audioUri).addOnSuccessListener {
            // Upload infographics
            uploadInfographics(goalId, goalName, goalDescription, audioPath)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload audio!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadInfographics(goalId: String, goalName: String, goalDescription: String, audioPath: String) {
        val infographicUrls = mutableListOf<String>()

        val uploadTasks = infographicUriList.map { uri ->
            val imagePath = "images/${UUID.randomUUID()}.jpg"
            storage.reference.child(imagePath).putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Upload failed")
                }
                storage.reference.child(imagePath).downloadUrl
            }.addOnSuccessListener { url ->
                infographicUrls.add(url.toString())
                // If all images are uploaded, add goal data to Firestore
                if (infographicUrls.size == infographicUriList.size) {
                    val goalData = hashMapOf(
                        "goal_name" to goalName,
                        "goal_description" to goalDescription,
                        "goal_images" to infographicUrls,
                        "audio_narration_url" to audioPath
                    )

                    // Add goal data to Firestore with specific document ID
                    firestore.collection("goals").document(goalId).set(goalData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data uploaded successfully!", Toast.LENGTH_SHORT).show()
                            clearFields()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload data!", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        // Check if all uploads are completed
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener {
            if (it.isSuccessful) {
                // All uploads were successful
            } else {
                Toast.makeText(this, "Some uploads failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        binding.editTextGoalName.text.clear()
        binding.editTextGoalDescription.text.clear()
        infographicUriList.clear()
        binding.recyclerViewInfographics.adapter?.notifyDataSetChanged()
        binding.recyclerViewInfographics.visibility = RecyclerView.GONE
        binding.textViewAudioFile.visibility = TextView.GONE
    }

    companion object {
        private const val IMAGE_REQUEST_CODE = 1000
        private const val AUDIO_REQUEST_CODE = 1001
    }
}
