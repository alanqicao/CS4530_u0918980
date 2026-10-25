package com.example.assignment_4_fun_facts.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient{
    val client = HttpClient(Android){
        install(ContentNegotiation){
            json(Json {
                isLenient = true
                ignoreUnknownKeys =true

            })
        }
    }
}