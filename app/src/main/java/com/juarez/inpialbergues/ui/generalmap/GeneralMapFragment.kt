package com.juarez.inpialbergues.ui.generalmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.juarez.inpialbergues.R
import com.juarez.inpialbergues.databinding.FragmentGeneralMapBinding

class GeneralMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGeneralMapBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGeneralMapBinding.inflate(inflater, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.generalMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}