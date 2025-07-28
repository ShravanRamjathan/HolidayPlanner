package com.holidayplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidayplanner.ui.theme.HolidayPlannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolidayPlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shrav's Holiday Planner")
        Spacer(modifier = Modifier.height(20.dp))
    AIResponseCard("some response")

    }
}

@Composable
fun SubmitButton(content: String, modifier: Modifier, onSubmit: () -> Unit) {
    ElevatedButton(modifier = modifier, onClick = { onSubmit() }) {
        Text(text = content)
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
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)) {
        Text(modifier = Modifier.fillMaxSize(0.9f).background(MaterialTheme.colorScheme.background), text = response, )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HolidayPlannerTheme {
        Scaffold { innerPadding ->
            HomeScreen(innerPadding)
        }
    }
}