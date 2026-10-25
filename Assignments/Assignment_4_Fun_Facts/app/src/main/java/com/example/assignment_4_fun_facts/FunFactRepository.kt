package com.example.assignment_4_fun_facts

import android.util.Log
import com.example.assignment_4_fun_facts.network.FunFactResponse
import com.example.assignment_4_fun_facts.room.FunFactDaoInterface
import com.example.assignment_4_fun_facts.room.FunFactEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class FunFactRepository(val scope: CoroutineScope, private val dao: FunFactDaoInterface, private val client: HttpClient) {

    val allFunFacts = dao.getAllFunFacts()

    fun searchFunFacts(keyword: String) = dao.searchFunFacts(keyword)

    fun fetchFunFact(){

            scope.launch {
                try {
                    val response = client.get("https://uselessfacts.jsph.pl/api/v2/facts/random?language=en").body<FunFactResponse>()
                    dao.insertFunFact(FunFactEntity(text = response.text))
                }catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e("FunFactRepository", "Error fetching or saving fun fact", e)
                }

            }
        }

    }
