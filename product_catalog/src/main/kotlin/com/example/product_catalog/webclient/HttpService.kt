package com.example.product_catalog.webclient

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class HttpService(private val webClient: WebClient) {

    fun httpCallService(tkn: String): String {
        val url = "http://localhost:8080/user/auth"
        val toString = webClient.get()
            .uri(url)
            .headers { headers ->
                headers.set(
                    "Authorization", "Bearer $tkn"
                )
            }.retrieve().bodyToMono(String::class.java)
        println("repsence is : ${toString.block()}")
        return toString.block().toString()
    }
}