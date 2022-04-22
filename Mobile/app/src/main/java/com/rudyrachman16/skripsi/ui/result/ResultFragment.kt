package com.rudyrachman16.skripsi.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.rudyrachman16.skripsi.BuildConfig
import com.rudyrachman16.skripsi.MainActivity
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.core.Status
import com.rudyrachman16.skripsi.databinding.FragmentResultBinding
import com.rudyrachman16.skripsi.ui.ViewModelFactory

class ResultFragment : Fragment() {
    private var binding: FragmentResultBinding? = null
    private val bind get() = binding!!
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (requireActivity() as MainActivity).setTitleToolbar(getString(R.string.prediction_result))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPrediction().observe(viewLifecycleOwner) {
            when (it) {
                is Status.Loading -> {
                    showLoading(true)
                    showError(false)
                }
                is Status.Error -> {
                    showLoading(false)
                    showError(true, it.error.message ?: "")
                }
                is Status.Success -> {
                    showLoading(false)
                    showError(false)
                    Glide.with(requireContext()).load(BuildConfig.BASE_URL + it.data.image_path)
                        .error(R.drawable.ic_broken_image)
                        .into(bind.resultObjectImage)
                    val adapter = ResultObjectAdapter()
                    adapter.submitList(it.data.objects)
                    bind.resultObjectRV.adapter = adapter
                    bind.resultObjectRV.setHasFixedSize(true)
                    bind.resultEmpty.visibility =
                        if (it.data.objects.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }


    private fun showLoading(isShow: Boolean) {
        bind.loadingProcessResult.root.visibility = if (isShow) View.VISIBLE else View.GONE
        bind.loadingProcessResult.loadingProcessText.text =
            getText(R.string.fetching_from_server)
    }

    private fun showError(isShow: Boolean, errorMessage: String = "") {
        bind.resultErrorMessage.visibility = if (isShow) View.VISIBLE else View.GONE
        bind.resultErrorMessage.text = errorMessage
    }

    override fun onDestroyView() {
        binding = null
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        super.onDestroyView()
    }
}