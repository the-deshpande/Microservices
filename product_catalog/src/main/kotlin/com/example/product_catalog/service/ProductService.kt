package com.example.product_catalog.service

import com.example.product_catalog.model.Product
import com.example.product_catalog.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun addProduct(product: Product): Product {
        // Generate a unique ID if it's not provided
        if (product.id == null) {
            product.id = UUID.randomUUID().toString()  // Generate a unique ID using UUID
        }

        productRepository.add(product)
        return product
    }

    fun updateProduct(id: String, product: Product): Product? {
        val existingProduct = productRepository.get(id) ?: return null
        existingProduct.name = product.name
        existingProduct.description = product.description
        existingProduct.price = product.price
        existingProduct.quantity = product.quantity

        productRepository.update(existingProduct)
        return existingProduct
    }


    fun deleteProduct(id: String) {
        val product = productRepository.get(id) ?: return
        productRepository.remove(product)  // Ensure the document includes both _id and _rev
    }

    fun getAllProducts(): List<Product> {
        return productRepository.getAll()
    }

    fun getProductById(id: String): Product? {
        return try {
            productRepository.get(id)  // Ensure this fetches the correct document by ID
        } catch (e: Exception) {
            null  // Handle exceptions when the document does not exist
        }
    }
}