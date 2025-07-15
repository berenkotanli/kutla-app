package com.example.kutlaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _friends = MutableLiveData<List<UserProfile>>()
    val friends: LiveData<List<UserProfile>> get() = _friends

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadFriends() {
        viewModelScope.launch {
            try {
                val friendsList = homeRepository.getFriends()
                _friends.value = friendsList
            } catch (e: Exception) {
                _error.value = "Arkadaş listesi alınamadı: ${e.message}"
            }
        }
    }
}
