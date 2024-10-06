package com.nitesh.app.sdgsclassroom

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.nitesh.app.sdgsclassroom.BottomFrag.GamesFragment
import com.nitesh.app.sdgsclassroom.BottomFrag.HomeFragment
import com.nitesh.app.sdgsclassroom.ChatBox.ChatBox
import com.nitesh.app.sdgsclassroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.community -> {
                    loadFragment(ChatBox())
                    true
                }
                R.id.game -> {
                    loadFragment(GamesFragment())
                    true
                }

                R.id.events -> {
                    Toast.makeText(this,"Available Soon",Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }


        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}