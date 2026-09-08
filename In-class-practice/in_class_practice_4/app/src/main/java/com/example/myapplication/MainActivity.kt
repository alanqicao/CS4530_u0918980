package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           Column(Modifier.statusBarsPadding()){
               ConcatenateApp()
           }
        }
    }
}

@Composable
fun ConcatenateApp(){
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp))
    {
        TextField(
            value = text1,
            onValueChange = { text1 = it },
            label = { Text("Input 1") }
        )
        TextField(
            value = text2,
            onValueChange = { text2 = it },
            label = { Text("Input 2") }
        )

        TextField(
            value = result,
            onValueChange = { result = it },
            label = { Text("Result") }
        )

        Button(onClick = {
            result = text1 + text2
        }) {
            Text("Concatenate")
        }

        Text(text = "Result: $result")
    }

}