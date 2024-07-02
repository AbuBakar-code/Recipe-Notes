package com.example.recipenotes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipenotes.dataClasses.Meal
import com.example.recipenotes.dataClasses.MealList
import com.example.recipenotes.database.MealDatabase
import com.example.recipenotes.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(private val mealDb: MealDatabase): ViewModel() {
    private var mealDetails = MutableLiveData<Meal>()

    fun getMealDetails(id: String){
        RetrofitInstance.apiMeal.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    mealDetails.value = response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("MealActivity", p1.message.toString())
            }

        })
    }

    fun observeMealDetails(): LiveData<Meal> {
        return mealDetails
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDb.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDb.mealDao().delete(meal)
        }
    }


}