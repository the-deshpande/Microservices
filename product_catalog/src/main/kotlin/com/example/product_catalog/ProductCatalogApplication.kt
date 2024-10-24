package com.example.product_catalog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ProductCatalogApplication

fun main(args: Array<String>) {
	runApplication<ProductCatalogApplication>(*args)
}