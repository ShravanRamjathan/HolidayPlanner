package com.holidayplanner

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import com.holidayplanner.service.CohereHoliday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cohereHoliday: CohereHoliday): ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeState())
    val homeUiState: StateFlow<HomeState> = _homeUiState.asStateFlow()

    fun submitInput(){

    }
}

data class HomeState(
    val response: TextFieldState = TextFieldState(),
    val input: TextFieldState = TextFieldState(),
    val errorMessage: String = "",
)