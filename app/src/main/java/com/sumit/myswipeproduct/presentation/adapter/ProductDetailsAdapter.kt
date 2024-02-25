package com.sumit.myswipeproduct.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sumit.myswipeproduct.R
import com.sumit.myswipeproduct.databinding.HomePageProductItemBinding
import com.sumit.myswipeproduct.domain.model.ProductItem

class ProductDetailsAdapter(
    private val items: MutableList<ProductItem?>?,
) : RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder>(), Filterable {

    private var productItems: MutableList<ProductItem?>? = items


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

                if (productData.image.isEmpty()) {
                    ivProductImage.load(R.drawable.ic_products_wine)
                } else {
                    ivProductImage.load(productData.image)
                }
            }
        }
    }

    fun updateData(data: List<ProductItem?>?) {
        productItems?.clear()
        productItems?.addAll(data ?: emptyList())
        notifyDataSetChanged()
    }

    fun addProduct(productItem: ProductItem) {
        productItems?.add(productItem)
        notifyItemInserted(productItems?.size ?: 0)
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


