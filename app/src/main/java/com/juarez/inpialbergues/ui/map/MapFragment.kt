package com.juarez.inpialbergues.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.juarez.inpialbergues.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val args: MapFragmentArgs by navArgs()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_HYBRID
        map?.uiSettings?.isZoomControlsEnabled = true
        val center = LatLng(18.6667, -96.9833)
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(center, 12f), 2000, null
        )
        val location = LatLng(args.house.latitude.toDouble(), args.house.longitude.toDouble())
        map?.addMarker(MarkerOptions().position(location).title(args.house.name))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}