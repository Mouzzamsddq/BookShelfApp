package com.example.bookshelfapp.ui.features.signin.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfapp.R
import com.example.bookshelfapp.base.Resource
import com.example.bookshelfapp.constants.StringConstant
import com.example.bookshelfapp.data.features.auth.repository.AuthRepo
import com.example.bookshelfapp.ui.features.signup.viewmodel.SignUpViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val _fieldValidationStatus = MutableLiveData<SignUpViewModel.FieldsValidationStatus>()
    val fieldsValidationStatus: LiveData<SignUpViewModel.FieldsValidationStatus> =
        _fieldValidationStatus

    private val _signInStatus = MutableLiveData<SignInStatus>()
    val signInStatus: LiveData<SignInStatus> = _signInStatus
    private var isNameValid = false
    private var isPasswordValid = false

    fun performSignIn(name: String, password: String) = viewModelScope.launch {
        _signInStatus.postValue(SignInStatus.Loading)
        delay(1000)
        val result = authRepo.performSignIn(name = name.trim().lowercase(), password = password)
        when (result.status) {
            Resource.Status.SUCCESS -> {
                authRepo.saveUserAuthenticated()
                _signInStatus.postValue(SignInStatus.Success)
            }

            Resource.Status.ERROR -> {
                _signInStatus.postValue(
                    SignInStatus.Error(
                        message = result.message ?: StringConstant.COMMON_ERROR_MESSAGE,
                    ),
                )
            }

            Resource.Status.LOADING -> {
                _signInStatus.postValue(SignInStatus.Loading)
            }
        }
    }

    fun checkNameValidation(name: String?, context: Context) {
        if (name.isNullOrBlank()) {
            isNameValid = false
            _fieldValidationStatus.postValue(
                SignUpViewModel.FieldsValidationStatus.NameError(
                    context.getString(
                        R.string.please_enter_valid_name,
                    ),
                ),
            )
        } else {
            isNameValid = true
            if (isPasswordValid) {
                _fieldValidationStatus.postValue(SignUpViewModel.FieldsValidationStatus.Success)
            } else {
                _fieldValidationStatus.postValue(SignUpViewModel.FieldsValidationStatus.NameSuccess)
            }
        }
    }

    fun checkPasswordValidation(password: String?, context: Context) {
        if (password.isNullOrBlank()) {
            isPasswordValid = false
            _fieldValidationStatus.postValue(
                SignUpViewModel.FieldsValidationStatus.PasswordError(
                    context.getString(
                        R.string.please_enter_valid_password,
                    ),
                ),
            )
        } else {
            isPasswordValid = true
            if (isNameValid) {
                _fieldValidationStatus.postValue(SignUpViewModel.FieldsValidationStatus.Success)
            } else {
                _fieldValidationStatus.postValue(SignUpViewModel.FieldsValidationStatus.PasswordSuccess)
            }
        }
    }

    sealed class SignInStatus {
        object Success : SignInStatus()
        data class Error(val message: String) : SignInStatus()
        object Loading : SignInStatus()
    }
}
