package com.example.recipenotes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipenotes.R
import com.example.recipenotes.activity.MainActivity
import com.example.recipenotes.activity.MealActivity
import com.example.recipenotes.adapter.FavoritesMealAdapter
import com.example.recipenotes.databinding.FragmentFavoritesBinding
import com.example.recipenotes.databinding.FragmentHomeBinding
import com.example.recipenotes.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: FavoritesMealAdapter

    companion object{
        const val MEAL_ID = "com.example.recipenotes.MEAL_ID"
        const val MEAL_NAME = "com.example.recipenotes.MEAL_NAME"
        const val MEAL_THUMB = "com.example.recipenotes.MEAL_THUMB"
        const val CATEGORY_NAME = "com.example.recipenotes.CATEGORY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeFavorites()
        onFavoriteItemClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])
                Toast.makeText(activity, "Meal Deleted", Toast.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecView)
    }

    private fun setUpRecyclerView() {
        favoritesAdapter = FavoritesMealAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoriteMealLiveData().observe(requireActivity()) { meal ->
            favoritesAdapter.differ.submitList(meal)
        }
    }

    private fun onFavoriteItemClick() {
        favoritesAdapter.onClick = {meals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meals.idMeal)
            intent.putExtra(MEAL_NAME, meals.strMeal)
            intent.putExtra(MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }

}