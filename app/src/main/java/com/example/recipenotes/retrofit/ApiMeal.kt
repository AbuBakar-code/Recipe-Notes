package com.example.recipenotes.retrofit

import com.example.recipenotes.dataClasses.CategoryList
import com.example.recipenotes.dataClasses.MealByCategoryList
import com.example.recipenotes.dataClasses.MealList
import com.example.recipenotes.dataClasses.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMeal {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularMeals(@Query("c") category: String): Call<MealByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") search: String): Call<MealList>
}