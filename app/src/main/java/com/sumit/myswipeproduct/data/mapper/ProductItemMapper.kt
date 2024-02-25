package com.sumit.myswipeproduct.data.mapper

import com.sumit.myswipeproduct.data.local.ProductItemEntity
import com.sumit.myswipeproduct.data.remote.dto.ProductDetailsDto
import com.sumit.myswipeproduct.domain.model.ProductItem


fun ProductDetailsDto.toProductItem() = ProductItem(
    product_name = product_name,
    product_type = product_type,
    tax = tax,
    price = price,
    image = image
)

fun ProductItem.toProductDetailsDto() = ProductDetailsDto(
    product_name = product_name,
    product_type = product_type,
    tax = tax,
    price = price,
    image = image
)


fun ProductItemEntity.toProductItem() = ProductItem(
    product_name = product_name,
    product_type = product_type,
    tax = tax,
    price = price,
    image = image
)

fun ProductItem.toProductItemEntity() = ProductItemEntity(
    product_name = product_name,
    product_type = product_type,
    tax = tax,
    price = price,
    image = image
)

fun <E> List<E?>?.toProductItemListGeneric(): List<ProductItem?> {
    return this?.mapNotNull {
        when (it) {
            is ProductItemEntity -> it.toProductItem()
            is ProductDetailsDto -> it.toProductItem()
            else -> null
        }
    } ?: emptyList()

}

fun <E> List<E?>?.toProductEntityListGeneric(): List<ProductItemEntity> {
    return this?.mapNotNull {
        when (it) {
            is ProductItem -> it.toProductItemEntity()
            is ProductDetailsDto -> it.toProductItem().toProductItemEntity()
            else -> null
        }
    } ?: emptyList()
}

