package com.rudyrachman16.skripsi.ui.menu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.rudyrachman16.skripsi.Injection
import com.rudyrachman16.skripsi.MainActivity
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var binding: FragmentMenuBinding? = null
    private val bind get() = binding!!
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        viewModel = MenuViewModel(Injection.provideRepository(requireContext()))
        return bind.root
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val name: String = viewModel.savePictureQ()
                if (name != "") requireActivity().applicationContext.deleteFile(name)
                val cr = requireContext().contentResolver
                val uri = viewModel.getTargetUri()
                if (uri != null) {
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(cr, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else MediaStore.Images.Media.getBitmap(cr, uri)
                    val resized = Bitmap.createScaledBitmap(bitmap, 512, 512, true)
                    // TODO: URI AS ARGS
                    bind.dummyTest.setImageBitmap(resized)
                }
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                if (uri != null) {
                    val cr = requireContext().contentResolver
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(cr, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else MediaStore.Images.Media.getBitmap(cr, uri)
                    val resized = Bitmap.createScaledBitmap(bitmap, 512, 512, true)
                    // TODO: URI AS ARGS
                    bind.dummyTest.setImageBitmap(resized)
                }
            }
        }

    private fun permissionCheck() {
        val granted = PackageManager.PERMISSION_GRANTED
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions[0]
                ) != granted && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions[1]
                ) != granted && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions[2]
                ) != granted
            ) ActivityCompat.requestPermissions(
                requireActivity(), permissions, MainActivity.REQUEST_CODE_PERMISSION
            ) else MainActivity.accepted = true

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions[2]
                ) != granted && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                ) != granted
            ) ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permissions[2], Manifest.permission.ACCESS_MEDIA_LOCATION),
                MainActivity.REQUEST_CODE_PERMISSION
            ) else MainActivity.accepted = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setTitleToolbar(getString(R.string.menu))
        bind.fromCamera.setOnClickListener {
            permissionCheck()
            if (MainActivity.accepted) {
                viewModel.getCameraIntent(cameraLauncher)
            }
        }
        bind.fromGallery.setOnClickListener {
            permissionCheck()
            if (MainActivity.accepted) {
                galleryLauncher.launch(Intent(Intent.ACTION_PICK).apply { type = "image/*" })
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}