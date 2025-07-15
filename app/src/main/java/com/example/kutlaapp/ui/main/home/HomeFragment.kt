package com.example.kutlaapp.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.kutlaapp.R
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.FragmentHomeBinding
import com.example.kutlaapp.ui.contacts.ContactsFragment
import com.example.kutlaapp.ui.extensions.calculateDaysUntilBirthday
import com.example.kutlaapp.ui.extensions.getCurrentDateFormattedForDisplay
import com.example.kutlaapp.ui.giftSuggestions.GiftSuggestionsFragment
import com.example.kutlaapp.ui.messageSuggestions.MessageSuggestionsFragment
import com.example.kutlaapp.ui.profile.FriendProfileBottomSheet
import com.example.kutlaapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        displayCurrentDate()

        binding.fabAddFriend.setOnClickListener {
            navigateToContactsFragment()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    filterList(it.toString())
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFriends()
    }

    private fun setupRecyclerView() {
        adapter = PersonAdapter(listOf()) { friend ->
            val bottomSheet = FriendProfileBottomSheet(friend)
            bottomSheet.show(parentFragmentManager, "FriendProfileBottomSheet")
        }
        binding.rvFriends.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFriends.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.friends.observe(viewLifecycleOwner) { friendsList ->
            handleFriendListVisibility(friendsList)
            friendsList.sortedBy { it.birthDate.calculateDaysUntilBirthday() }.let { adapter.updateList(it) }
            checkTodaysBirthday()
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFriendListVisibility(friendsList: List<UserProfile>) {
        if (friendsList.isEmpty()) {
            binding.llFriendList.visibility = View.GONE
            binding.llEmptyView.visibility = View.VISIBLE
        } else {
            binding.llFriendList.visibility = View.VISIBLE
            binding.llEmptyView.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkTodaysBirthday() {
        val today = Calendar.getInstance()
        val todayDate = today.get(Calendar.DAY_OF_MONTH)
        val todayMonth = today.get(Calendar.MONTH)

        val todayBirthday = viewModel.friends.value?.find { friend ->
            val birthCalendar = Calendar.getInstance()
            birthCalendar.timeInMillis = friend.birthDate
            birthCalendar.get(Calendar.DAY_OF_MONTH) == todayDate && birthCalendar.get(Calendar.MONTH) == todayMonth
        }

        todayBirthday?.let { friend ->
            binding.todayCard.visibility = View.VISIBLE
            binding.personName.text = friend.name
            binding.personAge.text = "Yaş: ${calculateAge(friend.birthDate)}"
            Glide.with(requireContext()).load(getAvatar(friend.gender)).into(binding.personImage)

            binding.btnSuggestGift.setOnClickListener {
                val bundle = Bundle().apply { putSerializable("friend", friend) }
                val giftFragment = GiftSuggestionsFragment().apply { arguments = bundle }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, giftFragment)
                    .addToBackStack(null)
                    .commit()
            }

            binding.btnSuggestMessage.setOnClickListener {
                val bundle = Bundle().apply { putSerializable("friend", friend) }
                val messageFragment = MessageSuggestionsFragment().apply { arguments = bundle }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, messageFragment)
                    .addToBackStack(null)
                    .commit()
            }
        } ?: run {
            binding.todayCard.visibility = View.GONE
        }
    }

    private fun calculateAge(birthDate: Long): Int {
        val birthCalendar = Calendar.getInstance()
        birthCalendar.timeInMillis = birthDate
        val birthYear = birthCalendar.get(Calendar.YEAR)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }

    private fun getAvatar(gender: String): Int {
        return if (gender == "Kadın") {
            R.drawable.avatar_women
        } else {
            R.drawable.avatar_man
        }
    }

    private fun displayCurrentDate() {
        val currentDate = getCurrentDateFormattedForDisplay()
        binding.currentDate.text = currentDate
    }

    private fun filterList(query: String) {
        viewModel.friends.value?.let { friendsList ->
            val filteredList = if (query.isEmpty()) {
                friendsList.sortedBy { it.birthDate.calculateDaysUntilBirthday() }
            } else {
                friendsList.filter { it.name.contains(query, ignoreCase = true) }
                    .sortedBy { it.birthDate.calculateDaysUntilBirthday() }
            }
            adapter.updateList(filteredList)
        }
    }

    private fun navigateToContactsFragment() {
        val fragment = ContactsFragment()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}