package com.nitesh.app.sdgsclassroom

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nitesh.app.sdgsclassroom.databinding.ActivityGoalDetailBinding

class Goal_Detail : AppCompatActivity() {
    lateinit var binding: ActivityGoalDetailBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var currentPosition = 0
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var infographicAdapter: InfographicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        val goalId = intent.getStringExtra("goalId") ?: return

        binding.btnAn.setOnClickListener {
           playAudioNarration(goalId)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvInfographics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        fetchGoalDetails(goalId)
    }

    private fun fetchGoalDetails(goalId: String) {
        firestore.collection("goals").document(goalId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Retrieve the data
                val goalName = document.getString("goal_name") ?: ""
                val goalDescription = document.getString("goal_description") ?: ""
                val goalImages = document.get("goal_images") as? List<String> ?: emptyList()
                val audioUrl = document.getString("audio_narration_url") ?: ""

                // Bind data to UI
                binding.goalname.text = goalName
                binding.goaldesc.text = goalDescription

                // Set up RecyclerView with infographics
                infographicAdapter = InfographicAdapter(goalImages)
                binding.rvInfographics.adapter = infographicAdapter
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        if (currentPosition == infographicAdapter.itemCount) {
                            currentPosition = 0
                        }
                        binding.rvInfographics.smoothScrollToPosition(currentPosition++)
                        handler.postDelayed(this, 2000) // Scroll every 3 seconds
                    }
                }
                handler.postDelayed(runnable, 2000)
            } else {
                Toast.makeText(this, "Goal not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAudioNarration(goalId: String) {


        if(goalId.equals("goal1")){
            mediaPlayer = MediaPlayer.create(this, R.raw.goal1)
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }else{
                mediaPlayer.pause()
            }
        }
        if(goalId.equals("goal2")){
            mediaPlayer = MediaPlayer.create(this, R.raw.goal2)
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }else{
                mediaPlayer.pause()
            }
        }
        if(goalId.equals("goal4")){
            mediaPlayer = MediaPlayer.create(this, R.raw.goal3)
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }else{
                mediaPlayer.pause()
            }
        }

        if(goalId.equals("goal4")){
            mediaPlayer = MediaPlayer.create(this, R.raw.goal4)
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }else{
                mediaPlayer.pause()
            }
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}