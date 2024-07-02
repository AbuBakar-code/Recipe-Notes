package com.example.recipenotes.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipenotes.R
import com.example.recipenotes.dataClasses.Meal
import com.example.recipenotes.database.MealDatabase
import com.example.recipenotes.databinding.ActivityMealBinding
import com.example.recipenotes.fragments.FavoritesFragment
import com.example.recipenotes.fragments.HomeFragment
import com.example.recipenotes.viewModel.HomeViewModel
import com.example.recipenotes.viewModel.MealViewModel
import com.example.recipenotes.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youTubeLink: String
    private lateinit var mealViewModel: MealViewModel
    private var isFavorite = false
    private var mealToSave: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = resources.getColor(R.color.accent)


        val mealDb = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDb)
        mealViewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInfoFromHomeFragment()
        setInfoInViewsFromHomeFragment()
        loadingCase()

        getMealInfoFromFavoriteFragment()
        setInfoInViewsFromFavoriteFragment()
        loadingCase()

        mealViewModel.getMealDetails(mealId)
        observerMealDetailsLiveData()

        onYouTubeImageClick()
        onFavoriteClick()
    }

//    This method will save the meal item in database
    private fun onFavoriteClick() {
        binding.addToFav.setOnClickListener {
            mealToSave?.let {
               if (isFavorite){
                   binding.addToFav.setImageResource(R.drawable.ic_baseline_save_24)
                   mealViewModel.deleteMeal(it)
                   Toast.makeText(this, "Meal Deleted", Toast.LENGTH_SHORT).show()
               }else{
                   binding.addToFav.setImageResource(R.drawable.baseline_check_24)
                   mealViewModel.insertMeal(it)
                   Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
               }
                isFavorite = !isFavorite
            }
        }
    }

//    This method will open the youtube link of meal item
    private fun onYouTubeImageClick(){
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youTubeLink))
            startActivity(intent)
        }
    }


//    This method observe the meal details live data and set the information in views
    private fun observerMealDetailsLiveData() {
        mealViewModel.observeMealDetails().observe(this)
        { value ->
            onResponseCase()
            binding.tvCategoryInfo.text = "Category: ${value.strCategory}"
            binding.tvAreaInfo.text = "Area: ${value.strArea}"
            binding.tvInstructions.text = value.strInstructions
            youTubeLink = value.strYoutube!!
            mealToSave = value
        }
    }

//    Here we are setting the information of meal item in views
    private fun setInfoInViewsFromHomeFragment() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }

//    This method will get the info of meal item from home fragment which will be displayed in this activity
    private fun getMealInfoFromHomeFragment() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    //    Here we are setting the information of meal item in views
    private fun setInfoInViewsFromFavoriteFragment() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }

    //    This method will get the info of meal item from home fragment which will be displayed in this activity
    private fun getMealInfoFromFavoriteFragment() {
        mealId = intent.getStringExtra(FavoritesFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(FavoritesFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(FavoritesFragment.MEAL_THUMB)!!
    }

//     Before get response from api all the views will be invisible except progressbar (will be visible)
    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvAreaInfo.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvContent.visibility = View.INVISIBLE
    }

//    When get response from api all the views will be visible except progressbar (will be invisible)
    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvAreaInfo.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvContent.visibility = View.VISIBLE
    }
}