package com.example.assignment_4_fun_facts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assignment_4_fun_facts.room.FunFactEntity
import com.example.assignment_4_fun_facts.ui.theme.Assignment_4_Fun_FactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val myVMObj: FunFactViewModel by viewModels { FunFactViewModelProvider.Factory }
        setContent {
            Assignment_4_Fun_FactsTheme {

                var searchKeyword by remember { mutableStateOf("") }

                val factsFlow = remember(myVMObj, searchKeyword) {
                    if (searchKeyword.isEmpty()) {
                        myVMObj.funFactReadOnly
                    } else {
                        myVMObj.searchFunFacts(searchKeyword)
                    }
                }

                val observerableFunFacts by factsFlow.collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FunFactScreen(
                        fetchFunFact = myVMObj::fetchFunFact,
                        observableFacts = observerableFunFacts,
                        modifier = Modifier.padding(innerPadding),
                        searchKeyword = searchKeyword,
                        onSearchKeywordChange = { searchKeyword = it },
                    )
                }
            }
        }
    }
}

@Composable
fun FunFactScreen(
    fetchFunFact: () -> Unit,
    observableFacts: List<FunFactEntity>,
    modifier: Modifier = Modifier,
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
) {

    Column(modifier.fillMaxWidth().padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = searchKeyword,
            onValueChange = onSearchKeywordChange,
            label = { Text("Search saved facts") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Row {
            Button(onClick = fetchFunFact) {
                Text("Fetch Fact")
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("FunFact List", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)

            LazyColumn(Modifier.weight(1f)) {
                items(observableFacts) { Text(it.text,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))}
            }

    }
}
