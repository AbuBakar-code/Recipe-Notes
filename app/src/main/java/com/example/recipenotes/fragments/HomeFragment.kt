package com.example.recipenotes.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipenotes.R
import com.example.recipenotes.activity.CategoryMeal
import com.example.recipenotes.activity.MainActivity
import com.example.recipenotes.activity.MealActivity
import com.example.recipenotes.adapter.CategoriesAdapter
import com.example.recipenotes.adapter.PopularMealAdapter
import com.example.recipenotes.dataClasses.Meal
import com.example.recipenotes.dataClasses.MealsByCategory
import com.example.recipenotes.databinding.FragmentHomeBinding
import com.example.recipenotes.fragments.bottomsheet.MealBottomSheetFragment
import com.example.recipenotes.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: PopularMealAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

//    Constants to be passed as extras to activities
    companion object{
        const val MEAL_ID = "com.example.recipenotes.MEAL_ID"
        const val MEAL_NAME = "com.example.recipenotes.MEAL_NAME"
        const val MEAL_THUMB = "com.example.recipenotes.MEAL_THUMB"
        const val CATEGORY_NAME = "com.example.recipenotes.CATEGORY_NAME"
    }

//    Here we are initializing the view model and popularAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = PopularMealAdapter()
    }

//    Here we are inflating the layout and setup binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

//    Here we are observing the view model and setting up the views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//    Setting up the recycler view for popular items
        preparePopularItemsRecyclerView()

//    For random meal we are calling the function
        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

//    For popular items we are calling the function
        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()
        onPopularItemLongClick()

//    For categories we are calling the function
        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onSearchClick()
    }

    private fun onSearchClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = {meal ->
            val bottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            bottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    //   These three methods for Category section
    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMeal::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recyclerViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoryLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategories(categories)
        })
    }

//    These three methods for Popular section
    private fun onPopularItemClick() {
        popularItemsAdapter.onClick = {meals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meals.idMeal)
            intent.putExtra(MEAL_NAME, meals.strMeal)
            intent.putExtra(MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdapter.setMealsList(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

//  These two methods for Random section
    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }
}