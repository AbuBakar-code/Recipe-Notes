package com.example.recipenotes.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipenotes.activity.MainActivity
import com.example.recipenotes.activity.MealActivity
import com.example.recipenotes.databinding.FragmentMealBottomSheetBinding
import com.example.recipenotes.fragments.HomeFragment
import com.example.recipenotes.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealById(it) }
        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheetDialog.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, mealId)
                putExtra(HomeFragment.MEAL_NAME, mealName)
                putExtra(HomeFragment.MEAL_THUMB, mealThumb)
            }
            startActivity(intent)
            fragmentManager?.popBackStack()
        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgCategory)
            binding.tvMealCountry.text = meal.strArea
            binding.tvMealCategory.text = meal.strCategory
            binding.tvMealName.text = meal.strMeal
            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}