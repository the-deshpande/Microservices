package com.example.product_catalog.controller

import com.example.product_catalog.model.Product
import com.example.product_catalog.service.ProductService
import com.example.product_catalog.webclient.HttpService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService, private val httpService: HttpService) {

    @PostMapping
    fun addProduct(http :HttpServletRequest, @RequestBody product: Product): ResponseEntity<Product> {
        var header = http.getHeader("Authorization")
        var username = httpService.httpCallService(header.substring(7))
        product.seller_name = username
        return ResponseEntity.ok(productService.addProduct(product))
    }

    @PutMapping("/{id}")
    fun updateProduct(http :HttpServletRequest, @PathVariable id: String, @RequestBody product: Product): ResponseEntity<Product?> {
        val updatedProduct = productService.updateProduct(id, product)
        var header = http.getHeader("Authorization")
        var username = httpService.httpCallService(header.substring(7))
        product.seller_name = username
        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(http :HttpServletRequest, @PathVariable id: String): ResponseEntity<Void> {
        var header = http.getHeader("Authorization")
        var username = httpService.httpCallService(header.substring(7))
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllProducts(http :HttpServletRequest): ResponseEntity<List<Product>> {
//        var header = http.getHeader("Authorization")
//        var username = httpService.httpCallService(header.substring(7))
        return ResponseEntity.ok(productService.getAllProducts())
    }

    @GetMapping("/{id}")
    fun getProductById(http :HttpServletRequest, @PathVariable id: String): ResponseEntity<Product?> {
        val product = productService.getProductById(id)
        var header = http.getHeader("Authorization")
        var username = httpService.httpCallService(header.substring(7))
        return if (product != null) {
            ResponseEntity.ok(product)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}