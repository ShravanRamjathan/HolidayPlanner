package com.holidayplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holidayplanner.model.Message
import com.holidayplanner.ui.theme.HolidayPlannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val homeViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolidayPlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(innerPadding, homeViewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(innerPadding: PaddingValues, viewModel: MainViewModel) {
    val uiState: HomeState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val loadingState = rememberSaveable { mutableStateOf(uiState.isLoading) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shrav's Holiday Planner")
        Spacer(modifier = Modifier.height(20.dp))
        AIResponse(uiState.messages, loadingState.value)
        Spacer(modifier = Modifier.height(20.dp))
        InputArea(
            "Holiday to china for 3 days",
            viewModel.inputField,
            onSubmit = { viewModel.submitInput() })
    }
}

@Composable
fun SubmitButton(modifier: Modifier, onSubmit: () -> Unit) {
    ElevatedButton(modifier = modifier, onClick = { onSubmit() }) {
        Icon(Icons.Default.ArrowOutward, contentDescription = "Submit button")
    }
}

@Composable
fun RetryButton(content: String, modifier: Modifier, onRetry: () -> Unit) {
    OutlinedButton(modifier = modifier, onClick = { onRetry() }) {
        Text(content)
    }
}

@Composable
fun AIResponse(messages: List<Message>, isLoading: Boolean) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
    ) {

        items(messages) { interaction ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                text = interaction.message, textAlign = TextAlign.Center,
            )
        }

    }
    LoadingAnimation(isLoading)

}

@Composable
fun LoadingAnimation(isLoading: Boolean) {
    var visibile by remember {
        mutableStateOf(isLoading)
    }
    AnimatedVisibility(
        visible = visibile,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.65f)
                .padding(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text("Generating response")
                CircularProgressIndicator(
                    modifier = Modifier.width(15.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Composable
fun InputArea(placeHolder: String, inputState: TextFieldState, onSubmit: () -> Unit) {
    Row {
        OutlinedTextField(
            state = inputState,
            placeholder = { Text(placeHolder) },
            modifier = Modifier.fillMaxWidth(0.65f)
        )
        SubmitButton(onSubmit = { onSubmit() }, modifier = Modifier)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HolidayPlannerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

        }

    }
}