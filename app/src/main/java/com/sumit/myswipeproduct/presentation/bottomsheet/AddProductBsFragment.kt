package com.sumit.myswipeproduct.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.FragmentAddProductBsBinding
import com.sumit.myswipeproduct.presentation.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductBsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductBsBinding? = null
    private val binding get() = _binding!!

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        bindObservers()
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialog
    }

    private fun bindObservers() {

    }

    private fun bindViews() {
        binding.tvSubmit.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productPrice = binding.etSellingPrice.text.toString()
            val productTax = binding.etTAx.text.toString()

            if (productName.isNotEmpty() && productPrice.isNotEmpty() && productTax.isNotEmpty() ) {
                homeScreenViewModel.addProduct(
                    productName,
                    productPrice,
                    productTax
                )
            }
        }
    }

    companion object {
        fun newInstance() = AddProductBsFragment()
    }

    interface AddProductListener {
        fun onProductAddedSuccess()
        fun onProductAddedFailure()
    }

}