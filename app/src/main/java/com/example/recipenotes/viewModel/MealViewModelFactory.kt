package com.example.recipenotes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipenotes.database.MealDatabase

class MealViewModelFactory(private val mealDB: MealDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDB) as T
    }
}