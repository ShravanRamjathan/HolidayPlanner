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
    val responseField: TextFieldState = TextFieldState()
    val inputField: TextFieldState = TextFieldState()
    private val _homeUiState = MutableStateFlow(HomeState())
    val homeUiState: StateFlow<HomeState> = _homeUiState.asStateFlow()


}

data class HomeState(

    val errorMessage: String = "",
)