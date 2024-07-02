package com.example.recipenotes.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipenotes.R
import com.example.recipenotes.adapter.CategoryMealAdapter
import com.example.recipenotes.databinding.ActivityCategoryMealBinding
import com.example.recipenotes.fragments.HomeFragment
import com.example.recipenotes.viewModel.CategoryMealViewModel

class CategoryMeal : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealBinding
    private lateinit var categoryViewModel: CategoryMealViewModel
    private lateinit var category: String
    private lateinit var categoryMealAdapter: CategoryMealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = getColor(R.color.accent)

        prepareRecyclerView()

        category = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!

        categoryViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CategoryMealViewModel::class.java)
        categoryViewModel.getCategoriesMeals(category)
        categoryViewModel.observeCategoryMealsLiveData().observe(this) { meals ->
            binding.tvCategoryCount.text = "Total Item: ${meals.size}"
            categoryMealAdapter.setMealList(meals)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.mealRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}