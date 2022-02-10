package com.juarez.inpialbergues.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.juarez.inpialbergues.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.cardHousesList.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToHousesFragment()
            it.findNavController().navigate(action)
        }
        binding.cardGeneralMap.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToGeneralMapFragment()
            it.findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}