package com.example.assignment_4_fun_facts.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fun_facts")
data class FunFactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
