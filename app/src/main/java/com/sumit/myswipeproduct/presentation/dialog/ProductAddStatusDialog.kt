package com.sumit.myswipeproduct.presentation.dialog

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.ProductAddStatusDialogFragmentBinding
import com.sumit.myswipeproduct.domain.model.ProductItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductAddStatusDialog : DialogFragment() {

    private var _binding: ProductAddStatusDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductAddStatusDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        setStyle(STYLE_NORMAL, R.style.CustomDialog)
    }

    private fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onStart() {
        super.onStart()
        setWidthPercent(90)
    }

    companion object {
        fun newInstance(data: ProductItem?): ProductAddStatusDialog {
            return ProductAddStatusDialog()

        }
    }

}