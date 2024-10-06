package com.nitesh.app.sdgsclassroom.BottomFrag

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitesh.app.sdgsclassroom.AdminUploadActivity
import com.nitesh.app.sdgsclassroom.R
import com.nitesh.app.sdgsclassroom.Upload_Image
import com.nitesh.app.sdgsclassroom.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTitle.setOnClickListener {
            var intent = Intent(requireContext(),AdminUploadActivity::class.java)
            startActivity(intent)
        }

        binding.textView.setOnClickListener {
            var intent = Intent(requireContext(),Upload_Image::class.java)
            startActivity(intent)
        }
        val goalsList = listOf(
            Goal("goal1","Goal 1", R.drawable.goal1),
            Goal("goal2","Goal 2", R.drawable.goal2),
            Goal("goal3","Goal 3", R.drawable.goal3),
            Goal("goal4","Goal 4", R.drawable.goal4),
            Goal("goal5","Goal 5", R.drawable.goal5),
            Goal("goal6","Goal 6", R.drawable.goal6),
            Goal("goal7","Goal 7", R.drawable.goal7),
            Goal("goal8","Goal 8", R.drawable.goal8),
            Goal("goal9","Goal 9", R.drawable.goal9),
            Goal("goal10","Goal 10", R.drawable.goal10),
            Goal("goal11","Goal 11", R.drawable.goal11),
            Goal("goal12","Goal 12", R.drawable.goal12),
        )
        val newsList = listOf(
            News("News 1: SDGs are progressing!", R.drawable.new2),
            News("News 2: Climate action on the rise.", R.drawable.news1),
            News("News 3: Education for all!", R.drawable.new3),
            News("News 4: Clean energy initiatives.", R.drawable.new2)
        )


        newsAdapter = NewsAdapter(newsList)
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvNews.adapter = newsAdapter
        binding.rvSDGS.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvSDGS.adapter = GoalAdapter(goalsList)

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (currentPosition == newsAdapter.itemCount) {
                    currentPosition = 0
                }
                binding.rvNews.smoothScrollToPosition(currentPosition++)
                handler.postDelayed(this, 2000) // Scroll every 3 seconds
            }
        }
        handler.postDelayed(runnable, 2000) // Start scrolling after 3 seconds
    }

}
