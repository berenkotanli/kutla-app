package com.example.kutlaapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    fun loadUserProfile(userId: String) {
        repository.getUserProfile(userId).observeForever {
            _userProfile.value = it
        }
    }

    fun updateUserProfile(userProfile: UserProfile, callback: (Boolean) -> Unit) {
        repository.updateUserProfile(userProfile, callback)
    }

    fun uploadProfileImage(userId: String, imageUri: Uri, callback: (String?) -> Unit) {
        repository.uploadProfileImage(userId, imageUri, callback)
    }

    fun getUserId(): String? {
        return repository.getUserId()
    }
}