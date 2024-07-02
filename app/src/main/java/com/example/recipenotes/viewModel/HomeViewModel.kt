package com.example.recipenotes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipenotes.dataClasses.Category
import com.example.recipenotes.dataClasses.CategoryList
import com.example.recipenotes.dataClasses.MealByCategoryList
import com.example.recipenotes.dataClasses.MealsByCategory
import com.example.recipenotes.dataClasses.Meal
import com.example.recipenotes.dataClasses.MealList
import com.example.recipenotes.database.MealDatabase
import com.example.recipenotes.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomeViewModel(private val mealDb: MealDatabase): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealLiveData = mealDb.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealLiveData = MutableLiveData<List<Meal>>()
    private val items = mutableListOf(
        "Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous",
        "Pasta", "SeaFood", "Side", "Starter", "Vegan",
        "Vegetarian", "Breakfast", "Goat"
    )
    private val randomCategory = items[Random.nextInt(items.size)]

    fun getRandomMeal(){
        RetrofitInstance.apiMeal.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.d("HomeFragment", "onFailure: ${p1.message}")
            }

        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun observeCategoryLiveData(): LiveData<List<Category>> {
        return categoryLiveData
    }

    fun getPopularItems(){
        RetrofitInstance.apiMeal.getPopularMeals(randomCategory).enqueue(object: Callback<MealByCategoryList>{
            override fun onResponse(p0: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if (response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }else{
                    return
                }
            }

            override fun onFailure(p0: Call<MealByCategoryList>, p1: Throwable) {
                Log.d("HomeFragment", "onFailure: ${p1.message}")
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.apiMeal.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(p0: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoryLiveData.postValue(it.categories)
                }
            }

            override fun onFailure(p0: Call<CategoryList>, p1: Throwable) {
                Log.d("HomeFragment", "onFailure: ${p1.message}")
            }

        })
    }

    fun observeFavoriteMealLiveData(): LiveData<List<Meal>> {
        return favoriteMealLiveData
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

    fun getMealById(id: String){
        RetrofitInstance.apiMeal.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meals->
                    bottomSheetMealLiveData.postValue(meals)
                }
            }

            override fun onFailure(p0: Call<MealList>, p1: Throwable) {
                Log.e("HomeFragment", "onFailure:${p1.message.toString()}")
            }
        })
    }

    fun searchMeal(searchQuery: String) = RetrofitInstance.apiMeal.searchMeals(searchQuery).enqueue(object : Callback<MealList>{
        override fun onResponse(p0: Call<MealList>, response: Response<MealList>) {
            val mealList = response.body()?.meals
            mealList?.let {
                searchedMealLiveData.postValue(it)
            }
        }

        override fun onFailure(p0: Call<MealList>, p1: Throwable) {
            Log.e("HomeFragment", "onFailure:${p1.message.toString()}")
        }
    })

    fun observeSearchedMealLiveData(): LiveData<List<Meal>> {
        return searchedMealLiveData
    }

    fun observeBottomSheetMealLiveData(): LiveData<Meal> {
        return bottomSheetMealLiveData
    }

}