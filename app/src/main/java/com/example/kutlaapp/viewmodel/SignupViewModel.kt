package com.example.kutlaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    private val _passwordValidationResult = MutableLiveData<String>()
    val passwordValidationResult: LiveData<String> get() = _passwordValidationResult

    fun validatePassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$".toRegex()
        return if (password.matches(regex)) {
            _passwordValidationResult.postValue("Şifre geçerli.")
            true
        } else {
            _passwordValidationResult.postValue("Şifre en az 8 karakter, bir büyük harf, bir küçük harf ve bir sembol içermelidir.")
            false
        }
    }
}
