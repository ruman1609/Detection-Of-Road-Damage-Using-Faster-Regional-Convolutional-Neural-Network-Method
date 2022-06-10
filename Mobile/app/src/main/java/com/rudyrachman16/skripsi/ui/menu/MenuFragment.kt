package com.rudyrachman16.skripsi.ui.menu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rudyrachman16.skripsi.MainActivity
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.core.Status
import com.rudyrachman16.skripsi.databinding.FragmentMenuBinding
import com.rudyrachman16.skripsi.ui.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MenuFragment : Fragment() {
    private var binding: FragmentMenuBinding? = null
    private val bind get() = binding!!
    private val viewModel by viewModels<MenuViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
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
                    showConfirmation(true)
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(cr, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else MediaStore.Images.Media.getBitmap(cr, uri)
                    val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
                    bind.resultImage.setImageBitmap(resized)
                }
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                if (uri != null) {
                    showConfirmation(true)
                    val cr = requireContext().contentResolver
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(cr, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else MediaStore.Images.Media.getBitmap(cr, uri)
                    val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
                    bind.resultImage.setImageBitmap(resized)
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
        showConfirmation(false)
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
        bind.cancelImage.setOnClickListener { showConfirmation(false) }
        bind.confirmImage.setOnClickListener {
            val bd = bind.resultImage.drawable as BitmapDrawable
            val bitmap = bd.bitmap
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(requireContext().cacheDir, "${timestamp}.jpg")
            lifecycleScope.launch(Dispatchers.Main) {
                showConfirmation(false)
                showLoading(true, isDefaultText = true)
                val createFile = async(Dispatchers.IO) {
                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    val bitmapData = bos.toByteArray()
                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(file)
                    } catch (e: Exception) {
                        showError(e)
                    } finally {
                        fos?.apply {
                            try {
                                write(bitmapData)
                                flush()
                                close()
                            } catch (e: Exception) {
                                showError(e)
                            }
                        }
                    }
                }
                createFile.await()
                viewModel.postPrediction(file).observe(viewLifecycleOwner) {
                    when (it) {
                        is Status.Loading -> showLoading(isShow = true, isDefaultText = false)
                        is Status.Success -> {
                            showLoading(false, isDefaultText = true)
                            requireActivity().applicationContext.deleteFile(file.name)
                            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToResultFragment())
                        }
                        is Status.Error -> {
                            showError(it.error)
                            showLoading(false, isDefaultText = true)
                            requireActivity().applicationContext.deleteFile(file.name)
                            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToResultFragment())
                        }
                    }
                }
            }
        }
    }

    private fun showConfirmation(isYes: Boolean) {
        bind.cancelImage.visibility = if (isYes) View.VISIBLE else View.GONE
        bind.confirmImage.visibility = if (isYes) View.VISIBLE else View.GONE
        bind.resultImage.visibility = if (isYes) View.VISIBLE else View.GONE

        bind.fromGallery.isEnabled = !isYes
        bind.fromCamera.isEnabled = !isYes
    }

    private fun showLoading(isShow: Boolean, isDefaultText: Boolean) {
        bind.loadingProcess.root.visibility = if (isShow) View.VISIBLE else View.GONE
        bind.loadingProcess.loadingProcessText.text =
            getText(if (isDefaultText) R.string.sending_to_server else R.string.fetching_from_server)
    }

    private fun showError(e: Exception) {
        e.printStackTrace()
        showError(e.message ?: "")
    }

    private fun showError(e: String) {
        Toast.makeText(
            requireContext(), "Error occured\n$e", Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}