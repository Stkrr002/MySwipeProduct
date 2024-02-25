package com.sumit.myswipeproduct.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sumit.myswipeproduct.databinding.HomePageProductItemBinding
import com.sumit.myswipeproduct.domain.model.ProductItem

class ProductDetailsAdapter(
    private val productItems: MutableList<ProductItem?>?,
) : RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        val binding = HomePageProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productItems?.size ?: 0
    }


    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        productItems?.get(position)?.let { holder.bind(it, position) }
    }

    inner class ProductDetailsViewHolder(private val binding: HomePageProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductItem, position: Int) {
            with(binding) {
                tvPrice.text = productData.price?.toString()
                tvProductName.text = productData.product_name
                tvProductType.text = productData.product_type
                tvTax.text = productData.tax?.toString()

                productData.image?.let {
                    ivProductImage.load(it)
                }
            }

        }
    }

    fun updateData(data: List<ProductItem?>?) {
        productItems?.clear()
        productItems?.addAll(data ?: emptyList())
        notifyDataSetChanged()
    }
}

