# Product Catalog Microservice with CouchDB Integration

## Overview

This document provides a detailed guide on how to set up a Spring Boot microservice for product catalog management with CouchDB as the database. The microservice exposes RESTful APIs for CRUD operations on products. CouchDB credentials are securely managed through `application.properties`.

## Prerequisites

1. **Java Development Kit (JDK)** - Version 8 or later
2. **Kotlin** - For Kotlin-based Spring Boot project
3. **Apache CouchDB** - CouchDB installed and running locally or remotely
4. **Spring Boot** - 2.x.x or later
5. **IDE** - IntelliJ IDEA, Eclipse, or any other IDE of your choice
6. **Postman** - To test API endpoints (optional)

## Technology Stack

- **Spring Boot** - For building the microservice
- **Kotlin** - Programming language
- **CouchDB** - NoSQL database for product data storage
- **Spring Data Ektorp** - CouchDB connector

---

## Application Structure

ecom-app

└── product_catalog
    
    ├── src
    │   └── main
    │       └── kotlin
    │           └── com
    │               └── example
    │                   └── product_catalog
    │                       ├── config
    │                       │   └── CouchDbConfig.kt
    |                       ├── controller
    |                       |   └── ProductController.kt
    │                       ├── model
    |                       |    └── Product.kt
    │                       ├── repository
    │                       |   └── ProductRepository.kt
    |                       ├── service
    |                       |   └── ProductService.kt
    │                       └── ProductCatalogApplication.kt
    └── resources
        └── application.properties


### Key Components

1. **CouchDBConfig**: Configures the connection to CouchDB.
2. **ProductController**: Exposes RESTful APIs to manage products.
3. **ProductService**: Contains business logic for managing products.
4. **ProductRepository**: Handles data persistence using CouchDB.
5. **Product**: Data model representing a product.

---

## Step-by-Step Guide

### 1. Set Up CouchDB

Install and run CouchDB on your local machine or server. By default, CouchDB runs on port `5984`. You can access CouchDB's dashboard at:

```http://localhost:5984/_utils```


Ensure you have created the necessary database (`product_catalog`) or it will be created automatically by the application.

### 2. Set Up Spring Boot Project

- Create a new Spring Boot project using Spring Initializer or your preferred method.
- Add the following dependencies in your `application.properties`:

```gradle
plugins {
	id("org.jetbrains.kotlin.jvm") version "1.9.25"
	id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.jetbrains.kotlin:kotlin-reflect'
	implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'
	implementation 'org.springframework.boot:spring-boot-starter-web'

	// CouchDB dependencies
	implementation 'org.ektorp:org.ektorp:1.5.0' // Ektorp library for CouchDB
	implementation 'com.fasterxml.jackson.module:jackson-module-kotlin'

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.named('test') {
	useJUnitPlatform()
}
```

---

### 3. Running the Application

1. Start your CouchDB instance.

2. Run your Spring Boot application.

3. Use Postman or cURL to interact with the API:

   - **POST** `/api/products`: Add a new product
   - **PUT** `/api/products/{id}`: Update an existing product
   - **DELETE** `/api/products/{id}`: Delete a product by ID
   - **GET** `/api/products`: Retrieve all products
   - **GET** `/api/products/{id}`: Retrieve a specific product by ID

#### Example POST Body

```json
{
  "name": "Laptop",
  "price": 1200.00,
  "quantity": 10,
  "description": "A high-performance laptop."
}
```

---

### Conclusion
This documentation outlines how to set up and configure a Spring Boot microservice that interacts with CouchDB using Kotlin. The approach secures sensitive credentials through application.properties and provides a modular architecture for managing product catalog data.


