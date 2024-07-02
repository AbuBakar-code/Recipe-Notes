package com.example.recipenotes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipenotes.database.MealDatabase

class HomeViewModelFactory(private val mealDB: MealDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDB) as T
    }
}