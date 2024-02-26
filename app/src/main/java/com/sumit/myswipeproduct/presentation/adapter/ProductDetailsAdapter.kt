package com.sumit.myswipeproduct.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.HomePageProductItemBinding
import com.sumit.myswipeproduct.domain.model.ProductItem

class ProductDetailsAdapter(
    private val items: MutableList<ProductItem?>?,
) : RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder>(), Filterable {

    private var productItems: MutableList<ProductItem?>?=null
        get() = items


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
        productItems?.get(position)?.let { holder.bind(it) }
    }

    inner class ProductDetailsViewHolder(private val binding: HomePageProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductItem) {
            with(binding) {
                tvPrice.text = "Price: ${productData.price?.toString()}"
                tvProductName.text = "Product Name: ${productData.product_name}"
                tvTax.text = "Tax: ${productData.tax?.toString()}"
                tvProductType.text = "Product Type: ${productData.product_type}"


                if (productData.image.isEmpty()) {
                    ivProductImage.setImageResource(R.drawable.ic_products_wine)
                    ivProductImage.scaleType = ImageView.ScaleType.FIT_CENTER
                } else {
                    Glide.with(binding.root.context)
                        .load(productData.image)
                        .into(ivProductImage)

                    ivProductImage.scaleType = ImageView.ScaleType.FIT_XY
                }
            }
        }
    }

    fun updateData(data: List<ProductItem?>?) {
        items?.clear()
        items?.addAll(data ?: emptyList())
        productItems?.clear()
        productItems?.addAll(data ?: emptyList())
        notifyDataSetChanged()
    }

    fun addProduct(productItem: ProductItem) {
        val validIndexToAdd = if (productItems?.size == 0) 0 else 1
        items?.add(validIndexToAdd, productItem)
        notifyItemInserted(validIndexToAdd)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase()

                val filteredList = if (queryString.isNullOrBlank()) {
                    items
                } else {
                    items?.filter {
                        it?.product_name?.lowercase()?.contains(queryString) == true
                    }
                }

                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productItems = (results?.values as? MutableList<ProductItem?>
                    ?: emptyList()) as MutableList<ProductItem?>?
                notifyDataSetChanged()
            }
        }
    }
}


