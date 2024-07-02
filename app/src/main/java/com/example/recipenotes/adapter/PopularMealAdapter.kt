package com.example.recipenotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipenotes.dataClasses.MealsByCategory
import com.example.recipenotes.databinding.PopularItemsBinding

class PopularMealAdapter(): RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {
    lateinit var onClick: (MealsByCategory) -> Unit
    var onLongItemClick: ((MealsByCategory) -> Unit)? = null
    private var mealList = ArrayList<MealsByCategory>()

    fun setMealsList(mealList: ArrayList<MealsByCategory>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        val list = mealList[position]
        Glide.with(holder.itemView)
            .load(list.strMealThumb)
            .into(holder.binding.popularImage)
        holder.itemView.setOnClickListener {
            onClick.invoke(list)
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(list)
            true
        }
    }

    class PopularMealViewHolder(var binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)
}