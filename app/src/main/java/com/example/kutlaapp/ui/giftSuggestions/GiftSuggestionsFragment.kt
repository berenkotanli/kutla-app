package com.example.kutlaapp.ui.giftSuggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.FragmentGiftSuggestionsBinding
import com.example.kutlaapp.utils.GiftSuggestionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GiftSuggestionsFragment : Fragment() {

    private var _binding: FragmentGiftSuggestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedFriend: UserProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiftSuggestionsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        selectedFriend = arguments?.getSerializable("friend") as? UserProfile
            ?: UserProfile()

        selectedFriend.let {
            fetchProductSuggestions(it)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            val adapter = binding.rvSuggestions.adapter as? GiftAdapter
            val likedProducts = adapter?.getLikedProducts() ?: emptyList()
            val dislikedProducts = adapter?.getDislikedProducts() ?: emptyList()

            if (likedProducts.isEmpty() && dislikedProducts.isEmpty()) {
                selectedFriend.let { fetchProductSuggestions(it) }
            } else {
                selectedFriend.let { fetchUpdatedProductSuggestions(it) }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchProductSuggestions(person: UserProfile) {
        lifecycleScope.launch {
            val productNames = GiftSuggestionHelper.getProductSuggestionsFromAI(person)
            binding.rvSuggestions.adapter = GiftAdapter(productNames) {}

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        binding.rvSuggestions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchUpdatedProductSuggestions(person: UserProfile) {
        lifecycleScope.launch {
            val likedProducts = (binding.rvSuggestions.adapter as? GiftAdapter)?.getLikedProducts() ?: emptyList()
            val dislikedProducts = (binding.rvSuggestions.adapter as? GiftAdapter)?.getDislikedProducts() ?: emptyList()

            val newProductNames = GiftSuggestionHelper.getFilteredProductSuggestionsFromAI(person, likedProducts, dislikedProducts)

            binding.rvSuggestions.adapter = GiftAdapter(newProductNames) {}

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}