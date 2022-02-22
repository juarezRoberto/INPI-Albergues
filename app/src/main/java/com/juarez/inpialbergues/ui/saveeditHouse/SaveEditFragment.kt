package com.juarez.inpialbergues.ui.saveeditHouse

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.databinding.FragmentSaveEditBinding
import com.juarez.inpialbergues.ui.MainViewModel
import com.juarez.inpialbergues.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SaveEditFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val args: SaveEditFragmentArgs by navArgs()
    private var _binding: FragmentSaveEditBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSaveEditBinding.inflate(inflater, container, false)
        binding.imgNewPhoto.setOnClickListener { requestPermissions() }

        if (args.isUpdating && args.house != null) {
            args.house?.let {
                binding.imgNewPhoto.loadPhoto(it.url)
                binding.outlinedHouseName.editText?.setText(it.name)
                binding.outlinedHouseAddress.editText?.setText(it.address)
                binding.outlinedHouseLatitude.editText?.setText(it.latitude)
                binding.outlinedHouseLongitude.editText?.setText(it.longitude)
            }
        }

        binding.btnUpload.setOnClickListener {
            val name = binding.outlinedHouseName.editText?.text
            val address = binding.outlinedHouseAddress.editText?.text
            val latitude = binding.outlinedHouseLatitude.editText?.text
            val longitude = binding.outlinedHouseLongitude.editText?.text

            if (
                name.toString().isEmpty() || address.toString().isEmpty() ||
                latitude.toString().isEmpty() || longitude.toString().isEmpty()
            ) {
                toast("Todos los datos son requeridos")
            } else {
                val newHouse = House(
                    name = name.toString(),
                    address = address.toString(),
                    latitude = latitude.toString(),
                    longitude = longitude.toString()
                )
                if (args.isUpdating) {
                    resetInputs()
                    args.house?.let {
                        viewModel.updateHouse(
                            newHouse.copy(id = it.id, url = it.url),
                            imageUri,
                            getFileExtension(imageUri)
                        )
                    }
                } else {
                    imageUri?.let {
                        resetInputs()
                        viewModel.saveHouse(newHouse, it, getFileExtension(it))
                    } ?: toast("La imagen es requerida")
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.saveHouseState.collect {
                when (it) {
                    is SaveHouseState.Loading -> whenLoading(it.isLoading)
                    is SaveHouseState.Success -> {
                        toast("guardado correctamente")
                        delay(1000)
                        requireActivity().onBackPressed()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateHouseState.collect {
                when (it) {
                    is UpdateHouseState.Loading -> whenLoading(it.isLoading)
                    is UpdateHouseState.Success -> {
                        toast("actualizado correctamente")
                        delay(1000)
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
        return binding.root
    }

    private fun whenLoading(isLoading: Boolean) {
        binding.progressSavingHouse.isVisible = isLoading
        binding.outlinedHouseName.isEnabled = !isLoading
        binding.outlinedHouseAddress.isEnabled = !isLoading
        binding.outlinedHouseLatitude.isEnabled = !isLoading
        binding.outlinedHouseLongitude.isEnabled = !isLoading
        binding.btnUpload.isEnabled = !isLoading
    }

    private fun resetInputs() {
        binding.outlinedHouseName.editText?.setText("")
        binding.outlinedHouseAddress.editText?.setText("")
        binding.outlinedHouseLatitude.editText?.setText("")
        binding.outlinedHouseLongitude.editText?.setText("")
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) requestGallery.launch("image/*")
            else toast(Constants.STORAGE_PERMISSION_DENIED)
        }

    private fun requestPermissions() {
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            when (it) {
                PermissionResult.GRANTED -> requestGallery.launch("image/*")
                PermissionResult.DENIED -> {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                PermissionResult.RATIONALE -> showRequestPermissionRationaleAlert()
            }
        }
    }

    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            imageUri?.let {
                binding.imgNewPhoto.setImageURI(it)
            }
        }

    private fun showRequestPermissionRationaleAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(Constants.ALERT_TITLE)
        builder.setMessage(Constants.STORAGE_PERMISSION_REQUIRED)
        builder.setPositiveButton(Constants.ALERT_OK) { dialog, _ ->
            dialog.dismiss()
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        builder.setNegativeButton(Constants.ALERT_CANCEL) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun getFileExtension(imageUri: Uri?): String? {
        if (imageUri == null) return null
        val resolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(resolver.getType(imageUri))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}