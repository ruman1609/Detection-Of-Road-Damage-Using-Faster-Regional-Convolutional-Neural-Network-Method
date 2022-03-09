package com.rudyrachman16.skripsi.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.rudyrachman16.skripsi.MainActivity
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var binding: FragmentMenuBinding? = null
    private val bind get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setTitleToolbar(getString(R.string.menu))
        bind.fromCamera.setOnClickListener {
            val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null) {
                        val imageBitmap = data.extras!!.get("data") as Bitmap
                    }
                }
            }
        }
        bind.fromGallery.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}