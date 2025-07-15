package com.example.kutlaapp.ui.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutlaapp.R
import com.example.kutlaapp.databinding.FragmentFriendsBinding
import com.example.kutlaapp.ui.extensions.calculateDaysUntilBirthday
import com.example.kutlaapp.ui.giftSuggestions.GiftSuggestionsFragment
import com.example.kutlaapp.ui.main.home.FriendsAdapter
import com.example.kutlaapp.ui.messageSuggestions.MessageSuggestionsFragment
import com.example.kutlaapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        val destination = arguments?.getString("destination")
        setupRecyclerView(destination)

        binding.titleTextView.text = when (destination) {
            "message" -> "Mesaj Önerisi Al"
            else -> "Hediye Önerisi Al"
        }

        observeViewModel()
        setupSearchListener()
        return binding.root
    }

    private fun setupRecyclerView(destination: String?) {
        adapter = FriendsAdapter(emptyList()) { friend ->
            val bundle = Bundle().apply { putSerializable("friend", friend) }
            val fragment = when (destination) {
                "message" -> MessageSuggestionsFragment().apply { arguments = bundle }
                else -> GiftSuggestionsFragment().apply { arguments = bundle }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.rvPeople.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPeople.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.friends.observe(viewLifecycleOwner) { friendsList ->
            if (friendsList.isNullOrEmpty()) {
                binding.llEmptyView.visibility = View.VISIBLE
                binding.titleTextView.visibility = View.GONE
                binding.searchEditText.visibility = View.GONE
                binding.rvPeople.visibility = View.GONE
            } else {
                binding.llEmptyView.visibility = View.GONE
                binding.titleTextView.visibility = View.VISIBLE
                binding.searchEditText.visibility = View.VISIBLE
                binding.rvPeople.visibility = View.VISIBLE

                val sortedList = friendsList.sortedBy { it.birthDate.calculateDaysUntilBirthday() }
                adapter.updateList(sortedList)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearchListener() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString()
                filterList(query)
            }
        })
    }

    private fun filterList(query: String) {
        viewModel.friends.value?.let { friendsList ->
            val filteredList = if (query.isEmpty()) {
                friendsList.sortedBy { it.birthDate.calculateDaysUntilBirthday() }
            } else {
                friendsList.filter { it.name.contains(query, ignoreCase = true) }
            }
            adapter.updateList(filteredList)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFriends()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}