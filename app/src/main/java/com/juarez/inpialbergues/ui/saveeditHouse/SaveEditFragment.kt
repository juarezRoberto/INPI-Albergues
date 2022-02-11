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
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.databinding.FragmentSaveEditBinding
import com.juarez.inpialbergues.ui.MainViewModel
import com.juarez.inpialbergues.utils.Constants
import com.juarez.inpialbergues.utils.PermissionResult
import com.juarez.inpialbergues.utils.requestPermission
import com.juarez.inpialbergues.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SaveEditFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentSaveEditBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSaveEditBinding.inflate(inflater, container, false)
        binding.imgNewPhoto.setOnClickListener { requestPermissions() }

        binding.btnUpload.setOnClickListener {
            val name = binding.outlinedHouseName.editText?.text
            val address = binding.outlinedHouseAddress.editText?.text
            if (name.toString().isEmpty() || address.toString().isEmpty() || imageUri == null) {
                toast("El nombre, la direccion y la imagen son requeridos")
            } else {
                val newHouse = House(name = name.toString(), address = address.toString())
                viewModel.saveHouse(newHouse, imageUri!!, getFileExtension(imageUri!!))
                binding.outlinedHouseName.editText?.setText("")
                binding.outlinedHouseAddress.editText?.setText("")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.saveHouseState.collect {
                when (it) {
                    is SaveHouseState.Loading -> {
                        binding.progressSavingHouse.isVisible = it.isLoading
                        binding.outlinedHouseName.isEnabled = !it.isLoading
                        binding.outlinedHouseAddress.isEnabled = !it.isLoading
                        binding.btnUpload.isEnabled = !it.isLoading
                    }
                    is SaveHouseState.Success -> {
                        toast("guardado correctamente")
                        delay(1000)
                        requireActivity().onBackPressed()
                    }
                }
            }
        }
        return binding.root
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

    private fun getFileExtension(imageUri: Uri): String? {
        val resolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(resolver.getType(imageUri))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}