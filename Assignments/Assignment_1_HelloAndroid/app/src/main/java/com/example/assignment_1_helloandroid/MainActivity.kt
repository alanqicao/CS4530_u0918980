package com.example.assignment_1_helloandroid

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.assignment_1_helloandroid.databinding.ActivityMainBinding

/**
 * Hosts the fragments used by the application.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}