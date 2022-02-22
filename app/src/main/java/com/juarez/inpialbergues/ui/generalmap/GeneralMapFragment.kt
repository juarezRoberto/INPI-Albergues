package com.juarez.inpialbergues.ui.generalmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.juarez.inpialbergues.databinding.FragmentGeneralMapBinding
import com.juarez.inpialbergues.ui.houses.HousesState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralMapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()
    private var _binding: FragmentGeneralMapBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGeneralMapBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(binding.generalMap.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lifecycleScope.launchWhenStarted {
            viewModel.housesState.collect { state ->
                when (state) {
                    is HousesState.Success -> {
                        binding.progressGeneralMap.isVisible = false
                        state.data.forEach {
                            val newPos = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                            map?.addMarker(MarkerOptions().position(newPos))
                        }
                    }
                    is HousesState.Loading -> binding.progressGeneralMap.isVisible = true
                    else -> Unit
                }
            }
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_HYBRID
        map?.uiSettings?.isZoomControlsEnabled = true
        val zongolica = LatLng(18.6667, -96.9833)
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(zongolica, 12f), 2000, null
        )
        map?.addMarker(MarkerOptions().position(zongolica))
        viewModel.getHouses()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}