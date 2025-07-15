package com.example.kutlaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kutlaapp.data.model.Contact
import com.example.kutlaapp.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    application: Application,
    private val contactsRepository: ContactsRepository
) : AndroidViewModel(application) {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    fun loadContacts() {
        viewModelScope.launch {
            val contactList = contactsRepository.getContacts(getApplication())
            _contacts.value = contactList
        }
    }

    fun addFriend(friendId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        contactsRepository.addFriend(friendId, onSuccess, onFailure)
    }
}