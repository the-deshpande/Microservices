package com.example.product_catalog.config

import org.ektorp.CouchDbConnector
import org.ektorp.CouchDbInstance
import org.ektorp.http.StdHttpClient
import org.ektorp.impl.StdCouchDbConnector
import org.ektorp.impl.StdCouchDbInstance
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CouchDBConfig {

    @Value("\${couchdb.url}")
    private lateinit var couchDbUrl: String

    @Value("\${couchdb.username}")
    private lateinit var couchDbUsername: String

    @Value("\${couchdb.password}")
    private lateinit var couchDbPassword: String

    @Bean
    fun couchDbConnector(): CouchDbConnector {
        val httpClient = StdHttpClient.Builder()
            .url(couchDbUrl)  // URL for CouchDB from application.properties
            .username(couchDbUsername)  // Username from application.properties
            .password(couchDbPassword)  // Password from application.properties
            .build()

        val dbInstance: CouchDbInstance = StdCouchDbInstance(httpClient)
        return StdCouchDbConnector("product_catalog", dbInstance).apply {
            createDatabaseIfNotExists()  // This will create the database if it doesn't exist
        }
    }
}
