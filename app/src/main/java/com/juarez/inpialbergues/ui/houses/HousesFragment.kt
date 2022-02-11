package com.juarez.inpialbergues.ui.houses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.juarez.inpialbergues.databinding.FragmentHousesBinding
import com.juarez.inpialbergues.models.House
import com.juarez.inpialbergues.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HousesFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHousesBinding? = null
    private val binding get() = _binding!!

    private val housesAdapter = HousesAdapter(::onClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHousesBinding.inflate(inflater, container, false)
        binding.recyclerHouses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = housesAdapter
            setHasFixedSize(true)
        }

        binding.fabAddHouse.setOnClickListener {
            val action = HousesFragmentDirections.actionHousesFragmentToSaveEditFragment()
            it.findNavController().navigate(action)
        }

        viewModel.getHouses()
        lifecycleScope.launchWhenStarted {
            viewModel.housesState.collect {
                when (it) {
                    is HousesState.Success -> {
                        housesAdapter.submitList(it.data)
                        binding.progressHouses.isVisible = false
                    }
                    is HousesState.Error -> {
                        binding.progressHouses.isVisible = false
                    }
                    is HousesState.Loading -> binding.progressHouses.isVisible = true
                    else -> Unit
                }
            }
        }
        return binding.root
    }

    private fun onClickListener(house: House) {
        val action = HousesFragmentDirections.actionHousesFragmentToMapFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}