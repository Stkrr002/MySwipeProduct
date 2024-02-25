package com.sumit.myswipeproduct.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.FragmentAddProductBsBinding
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.presentation.HomeScreenViewModel
import com.sumit.myswipeproduct.responsehandler.APIResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductBsFragment(private val listener: AddProductListener) : BottomSheetDialogFragment() {

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
        homeScreenViewModel.addProductData.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Loading -> {
                    dialog?.setCancelable(false)
                    binding.progressBarLayoutLoader.visibility = View.VISIBLE
                }

                is APIResponse.Success -> {
                    handleLoadingState(false)
                    binding.progressBarLayoutLoader.visibility = View.GONE
                    listener.onProductAddedSuccess(it.data)
                    dismiss()
                }

                is APIResponse.Error -> {
                    handleLoadingState(false)
                    binding.progressBarLayoutLoader.visibility = View.GONE
                    listener.onProductAddedFailure(it.message)
                }
            }
        }

    }

    private fun handleLoadingState(isLoading: Boolean) {
        if (isLoading) {
            dialog?.setCancelable(false)
            binding.progressBarLayoutLoader.visibility = View.VISIBLE
        } else {
            dialog?.setCancelable(true)
            binding.progressBarLayoutLoader.visibility = View.GONE
        }
    }

    private fun bindViews() {
        binding.tvSubmit.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {

        //todo create function to validate all fields
        val productName = binding.etProductName.text.toString()
        val productPrice = binding.etSellingPrice.text.toString()
        val productTax = binding.etTAx.text.toString()

        if (productName.isNotEmpty() && productPrice.isNotEmpty() && productTax.isNotEmpty()) {
            homeScreenViewModel.addProduct(
                productName,
                productPrice,
                productTax
            )
        }
    }

    companion object {
        fun newInstance(listener: AddProductListener) = AddProductBsFragment(listener)
    }

    interface AddProductListener {
        fun onProductAddedSuccess(data: ProductItem?)
        fun onProductAddedFailure(message: String?)
    }

}