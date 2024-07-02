package com.example.recipenotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipenotes.dataClasses.Meal
import com.example.recipenotes.databinding.MealItemsBinding

class FavoritesMealAdapter: RecyclerView.Adapter<FavoritesMealAdapter.FavoritesMealViewHolder>() {

    lateinit var onClick: (Meal) -> Unit
    private val diffUtils = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtils)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealViewHolder {
        return FavoritesMealViewHolder(MealItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesMealViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal
        holder.itemView.setOnClickListener{
            onClick.invoke(meal)
        }
    }

    class FavoritesMealViewHolder(val binding: MealItemsBinding): RecyclerView.ViewHolder(binding.root)
}