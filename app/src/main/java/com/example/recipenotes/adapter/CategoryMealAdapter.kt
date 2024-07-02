package com.example.recipenotes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipenotes.dataClasses.MealsByCategory
import com.example.recipenotes.databinding.MealItemsBinding

class CategoryMealAdapter: RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {

    private var mealList = ArrayList<MealsByCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMealList(mealsList: List<MealsByCategory>) {
        this.mealList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    class CategoryMealViewHolder(val binding: MealItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(MealItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        val category = mealList[position]
        Glide.with(holder.itemView)
            .load(category.strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = category.strMeal
    }

}