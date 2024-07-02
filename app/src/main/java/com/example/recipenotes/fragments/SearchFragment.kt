package com.example.recipenotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipenotes.activity.MainActivity
import com.example.recipenotes.adapter.FavoritesMealAdapter
import com.example.recipenotes.databinding.FragmentSearchBinding
import com.example.recipenotes.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: FavoritesMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        binding.icSearch.setOnClickListener{
            searchMeals()
        }
        observeSearchMealsLiveData()

        var searchJob : Job? = null
        binding.edSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(it.toString())
            }
        }

    }

    private fun observeSearchMealsLiveData() {
        viewModel.observeSearchedMealLiveData().observe(viewLifecycleOwner, Observer { mealList ->
            searchRecyclerViewAdapter.differ.submitList(mealList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearch.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = FavoritesMealAdapter()
        binding.recyclerViewSearch.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }

}