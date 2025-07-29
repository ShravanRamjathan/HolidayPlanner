package com.holidayplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holidayplanner.ui.theme.HolidayPlannerTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue


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
fun HomeScreen(innerPadding: PaddingValues,viewModel: MainViewModel ) {
    val uiState: HomeState by viewModel.homeUiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shrav's Holiday Planner")
        Spacer(modifier = Modifier.height(20.dp))
        AIResponseCard("some response")
        Spacer(modifier = Modifier.height(20.dp))
        InputArea("Holiday to china for 3 days",uiState.input ) { }
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
fun AIResponseCard(response: String) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary)) {
        Text(modifier = Modifier.fillMaxSize(0.9f), text = response)
    }
}

@Composable
fun InputArea(placeHolder: String, inputState: TextFieldState, onSubmit: () -> Unit) {
    Row {
        TextField(state = inputState, placeholder = { Text(placeHolder) })
        SubmitButton(onSubmit = { onSubmit() }, modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HolidayPlannerTheme {

    }
}