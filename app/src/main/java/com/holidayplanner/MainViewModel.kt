package com.holidayplanner

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holidayplanner.model.HolidayPlanner
import com.holidayplanner.model.Message
import com.holidayplanner.model.Prompt
import com.holidayplanner.model.UseCase
import com.holidayplanner.service.CohereHoliday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val cohereHoliday: CohereHoliday) : ViewModel() {

    val inputField: TextFieldState =  TextFieldState()
    private val _homeUiState = MutableStateFlow(HomeState())
    val homeUiState: StateFlow<HomeState> = _homeUiState.asStateFlow()

    fun updateMessages(message: Message) {
        val updatedList = when {
            message.message.isNotEmpty() -> _homeUiState.value.messages + message
            else -> throw Exception("There is no valid response message")
        }
        _homeUiState.update { currentState ->
            currentState.copy(messages = updatedList)
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
             val userMessage = Message(
                 message = inputField.text.toString(),
                 userCase = UseCase.USER,
                 dateTime = LocalDateTime.now()
             )
                Log.d("Myapi", "Attempting")
                val prompt = Prompt(
                    prompt = inputField.text.toString()
                )
                updateMessages(userMessage)
                clearTextField()
                Log.d("Myapi", prompt.prompt)
                val holidayPlanner: HolidayPlanner =
                    cohereHoliday.generateHolidayPlan(prompt)
                if (holidayPlanner.text.isEmpty()) {
                    notLoading()
                    _homeUiState.update { currentState ->
                        currentState.copy(errorMessage = "Unable to fetch a response, ssh into the server :)")
                    }
                }
                Log.d(
                    "Myapi",
                    "THis is fresh from the api" + holidayPlanner.text
                )  // lets see if there is something
                val responseMessage = Message(
                    message = holidayPlanner.text,
                    userCase = UseCase.AI,
                    dateTime = LocalDateTime.now()
                )
                updateMessages(responseMessage)
                notLoading()


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
    val messages: List<Message> = emptyList<Message>(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)