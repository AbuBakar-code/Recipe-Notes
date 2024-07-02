package com.example.recipenotes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipenotes.dataClasses.MealByCategoryList
import com.example.recipenotes.dataClasses.MealsByCategory
import com.example.recipenotes.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel: ViewModel() {
    val categoryMealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getCategoriesMeals(categoryName: String) {
        RetrofitInstance.apiMeal.getMealsByCategory(categoryName).enqueue(object : Callback<MealByCategoryList>{
            override fun onResponse(p0: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                response.body()?.let {
                    categoryMealsLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(p0: Call<MealByCategoryList>, p1: Throwable) {
                Log.d("CategoryMealsViewModel", p1.message.toString())
            }

        })
    }

    fun observeCategoryMealsLiveData(): LiveData<List<MealsByCategory>> {
        return categoryMealsLiveData
    }
}