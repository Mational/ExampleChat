package com.example.examplechat.presentation.common

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<T>(initialState: T) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<T> = _uiState

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    protected fun updateState(update: (T) -> T) {
        _uiState.update(update)
    }

    protected fun updateState(newState: T) {
        _uiState.value = newState
    }

    protected fun navigateTo(route: String) {
        _navigationEvent.value = route
    }

    protected fun debugLogger(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}