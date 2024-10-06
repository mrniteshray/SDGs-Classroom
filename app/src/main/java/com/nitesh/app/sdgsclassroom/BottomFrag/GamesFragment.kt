package com.nitesh.app.sdgsclassroom.BottomFrag

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nitesh.app.sdgsclassroom.SpinActivity
import com.nitesh.app.sdgsclassroom.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {

    private var _binding : FragmentGamesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.queryHint = "Search...."
        binding.btnSpinWheel.setOnClickListener {
            startActivity(Intent(requireContext(),SpinActivity::class.java))
        }
    }

}