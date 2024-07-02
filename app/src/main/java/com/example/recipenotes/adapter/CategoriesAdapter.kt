package com.example.recipenotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipenotes.dataClasses.Category
import com.example.recipenotes.databinding.CategoryItemBinding

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> (){
    var onItemClick: ((Category) -> Unit)? = null
    private var categoriesList = ArrayList<Category>()

    fun setCategories(categories: List<Category>) {
        this.categoriesList = categories as ArrayList<Category>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categoriesList[position]
        Glide.with(holder.itemView)
            .load(category.strCategoryThumb)
            .into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text = category.strCategory
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(category)
        }
    }

    class CategoriesViewHolder(var binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)
}