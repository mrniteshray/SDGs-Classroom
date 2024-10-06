package com.nitesh.app.sdgsclassroom

import android.animation.Animator
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nitesh.app.sdgsclassroom.databinding.ActivitySpinBinding

class SpinActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpinBinding
    private lateinit var mediaPlayer: MediaPlayer

    private val facts = listOf(
        "Over 700 million people still live in extreme poverty, surviving on less than $1.90 a day.",
        "Nearly 690 million people are hungry, and the COVID-19 pandemic has worsened food insecurity.",
        "Around 5 million children under the age of five die each year from preventable diseases.",
        "More than 260 million children and youth are out of school globally.",
        "1 in 3 women experience physical or sexual violence in their lifetime.",
        "Around 2 billion people live without access to safe drinking water.",
        "Over 800 million people still lack access to electricity.",
        "Approximately 200 million people are unemployed globally.",
        "Over 1.6 billion people lack access to reliable electricity.",
        "The richest 10% earn up to 40% of total global income, while the poorest earn only 2%."
    )

    lateinit var randomfact : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStart.setOnClickListener {
            binding.spinWheelAnimation.playAnimation()
            mediaPlayer = MediaPlayer.create(this, R.raw.spinwheelsound)
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }else{
                mediaPlayer.pause()
            }// Start the spin animation
            binding.spinWheelAnimation.setSpeed(2f)
            binding.spinWheelAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }
                override fun onAnimationEnd(animation: Animator) {
                    randomfact = facts.random()
                    showFactDialog(randomfact)
                    mediaPlayer.stop()
                }

                override fun onAnimationCancel(animation: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun showFactDialog(fact: String) {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_fact, null)
        dialog.setContentView(dialogView)

        val factTextView = dialogView.findViewById<TextView>(R.id.factTextView)
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeButton)

        factTextView.text = fact

        closeButton.setOnClickListener {
            dialog.dismiss()
            randomfact = ""
        }

        dialog.show() // Display the dialog
    }
}