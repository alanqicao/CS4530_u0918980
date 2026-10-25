package com.example.assignment_4_fun_facts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDaoInterface{
    @Insert
    suspend fun insertFunFact(funFact: FunFactEntity)

    @Query("SELECT * FROM fun_facts ORDER BY timestamp DESC, id DESC")
    fun getAllFunFacts(): Flow<List<FunFactEntity>>

    @Query("SELECT * FROM fun_facts WHERE text LIKE '%' || :keyword || '%' ORDER BY timestamp DESC, id DESC")
    fun searchFunFacts(keyword: String): Flow<List<FunFactEntity>>
}