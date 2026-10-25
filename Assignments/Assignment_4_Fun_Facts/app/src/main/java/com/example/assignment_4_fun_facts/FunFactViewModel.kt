package com.example.assignment_4_fun_facts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.assignment_4_fun_facts.room.FunFactEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.listOf

class FunFactViewModel(private val repository: FunFactRepository) : ViewModel()  {

    val funFactReadOnly: StateFlow<List<FunFactEntity>> = repository.allFunFacts.stateIn(
        scope = repository.scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf() )

    fun searchFunFacts(keyword: String): Flow<List<FunFactEntity>> {
        return repository.searchFunFacts(keyword)
    }

    fun fetchFunFact(){
        repository.fetchFunFact()
    }
}

object FunFactViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FunFactViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FunFactApplication).repository
            )
        }
    }

}