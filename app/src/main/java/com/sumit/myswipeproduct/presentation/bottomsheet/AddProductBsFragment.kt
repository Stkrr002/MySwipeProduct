package com.sumit.myswipeproduct.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.FragmentAddProductBsBinding
import com.sumit.myswipeproduct.domain.model.ProductItem
import com.sumit.myswipeproduct.presentation.HomeScreenViewModel
import com.sumit.myswipeproduct.responsehandler.APIResponse
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class AddProductBsFragment(private val listener: AddProductListener) : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductBsBinding? = null
    private val binding get() = _binding!!

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    private var selectedImageUri: Uri? = null

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
        handleProductTypeSpinner()
        handleProductImageWidget()
        binding.tvSubmit.setOnClickListener {
            addProduct()
        }
    }

    private fun handleProductImageWidget() {
        binding.ivProductImage.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivProductImage.setImageURI(selectedImageUri)
            }
        }
    }


    private fun handleProductTypeSpinner() {
        val productTypeList = getProductTypes()

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                productTypeList
            )
        binding.actProductType.setAdapter(adapter)
    }

    private fun getProductTypes(): List<String> {
        return listOf("Product Type 1", "Product Type 2", "Product Type 3")
    }


    private fun addProduct() {

        //todo create function to validate all fields
        val productName = binding.etProductName.text?.toString()
        val productPrice = binding.etSellingPrice.text?.toString()
        val productTax = binding.etTAx.text?.toString()
        val productType = binding.actProductType.text?.toString()
        val productImage = selectedImageUri?.let { getImageFile(it) }

        if (productName?.isNotEmpty() == true && productPrice?.isNotEmpty() == true && productTax?.isNotEmpty() == true && productType?.isNotEmpty() == true) {
            homeScreenViewModel.addProduct(
                productName,
                productPrice,
                productTax,
                productType,
                productImage
            )
        }
    }

    private fun getImageFile(uri: Uri): File? {
        val context = requireContext()
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "image${System.currentTimeMillis()}.jpg"

        inputStream?.use { input ->
            val file = File(context.cacheDir, fileName)
            file.outputStream().use { output ->
                input.copyTo(output)
                return file
            }
        }

        return null
    }

    private fun getProductImage(selectedImageUri: Uri?): MultipartBody.Part? {
        selectedImageUri?.let { uri ->
            val file = uri.path?.let { File(it) }
            val requestFile =
                file?.let { it.asRequestBody("multipart/form-data".toMediaTypeOrNull()) }
            return requestFile?.let { MultipartBody.Part.createFormData("image", file.name, it) }
        }
        return null
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 100
        fun newInstance(listener: AddProductListener) = AddProductBsFragment(listener)
    }

    interface AddProductListener {
        fun onProductAddedSuccess(data: ProductItem?)
        fun onProductAddedFailure(message: String?)
    }

}