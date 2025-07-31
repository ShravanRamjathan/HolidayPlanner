package com.holidayplanner

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidayplanner.model.HolidayPlanner
import com.holidayplanner.model.Prompt
import com.holidayplanner.service.CohereHoliday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cohereHoliday: CohereHoliday) : ViewModel() {

    val inputField: TextFieldState = TextFieldState()
    private val _homeUiState = MutableStateFlow(HomeState())
    val homeUiState: StateFlow<HomeState> = _homeUiState.asStateFlow()

    fun updateResponseMessages(response: String) {
        val updatedList = when {
            response.isNotEmpty() -> _homeUiState.value.responseMessages + response
            else -> throw Exception("There is no valid response message")
        }
        _homeUiState.update { currentState ->
            currentState.copy(responseMessages = updatedList)
        }
    }

    fun isLoading() {
        _homeUiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
    }

    fun notLoading() {
        _homeUiState.update { currentState ->
            currentState.copy(isLoading = false)
        }
    }

    fun submitInput() {
        viewModelScope.launch {
            isLoading()
            try {
                Log.d("Myapi", "Attempting")
                val prompt: Prompt = Prompt(
                    prompt = inputField.text.toString()
                )
                val holidayPlanner: HolidayPlanner =
                    cohereHoliday.generateHolidayPlan(prompt)
                Log.d(
                    "Myapi",
                    "THis is fresh from the api" + holidayPlanner.text
                )  // lets see if there is something
                updateResponseMessages(holidayPlanner.text)
                clearTextField()
            } catch (e: Exception) {
                Log.e("Myapi", e.message, e)
            }
        }
    }

    fun clearTextField() {
        inputField.clearText()
    }


}

data class HomeState(
    val responseMessages: List<String> = emptyList<String>(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)