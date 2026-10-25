package com.example.assignment_4_fun_facts

import android.app.Application
import androidx.room.Room
import com.example.assignment_4_fun_facts.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.getValue
import com.example.assignment_4_fun_facts.room.FunFactDatabase

class FunFactApplication: Application() {

    val scope = CoroutineScope(SupervisorJob())

    val database by lazy {
        Room.databaseBuilder(
        applicationContext,
        FunFactDatabase::class.java,
        "fun_fact_database").build() }

    val client by lazy { ApiClient.client }

    val repository by lazy { FunFactRepository(scope,database.funFactDao(), client) }
}