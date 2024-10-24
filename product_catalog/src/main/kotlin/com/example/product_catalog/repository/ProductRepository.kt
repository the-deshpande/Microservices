package com.example.product_catalog.repository

import com.example.product_catalog.model.Product
import org.ektorp.CouchDbConnector
import org.ektorp.support.CouchDbRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val db: CouchDbConnector) : CouchDbRepositorySupport<Product>(Product::class.java, db) {

    init {
        // This replaces the @PostConstruct method
        try {
            initStandardDesignDocument()
        } catch (e: Exception) {
            println("Error initializing design document: ${e.message}")
        }
    }

    // Example of a custom query to find products by name
    fun findByName(name: String): List<Product> {
        return queryView("by_name", name)
    }
}