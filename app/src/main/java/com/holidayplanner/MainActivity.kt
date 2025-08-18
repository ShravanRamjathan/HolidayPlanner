package com.holidayplanner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holidayplanner.model.Message
import com.holidayplanner.model.UseCase
import com.holidayplanner.ui.theme.HolidayPlannerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ElevatedCard { Text("Shrav's Holiday Planner") }
        Spacer(modifier = Modifier.height(20.dp))
        AIResponse(uiState.messages, uiState.isLoading)
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
    OutlinedCard(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth(0.95f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            state = listState
        ) {

            items(messages) { interaction ->
                var visible by remember { mutableStateOf(false) }
                val alignment: Alignment = when {
                    interaction.userCase == UseCase.AI ->
                        Alignment.CenterStart

                    interaction.userCase == UseCase.USER -> Alignment.CenterEnd
                    else -> Alignment.CenterEnd
                }
                val color = if (interaction.userCase == UseCase.AI) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.tertiary
                }

                Spacer(Modifier.height(10.dp))

                Box(
                    contentAlignment = alignment,
                    modifier = Modifier
                        .isElementVisible({ visible })
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Log.d("Myapi", interaction.userCase.name)
                    ResponseCard(interaction.message, color, isLoading)
                }


            }
            if (isLoading) {
                item {
                    Spacer(Modifier.height(10.dp))
                    Box(
                        contentAlignment = Alignment.CenterStart, // Center the loading animation
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp) // Add some vertical padding
                    ) {
                        LoadingAnimation(isLoading)
                    }

                }
                coroutineScope.launch {
                    listState.animateScrollToItem(index = messages.size)
                }
            }
        }

    }
}

@Composable
fun LoadingAnimation(isLoading: Boolean) {

    AnimatedVisibility(
        visible = isLoading,
        enter = slideInHorizontally(animationSpec = tween(500)),
        exit = slideOutHorizontally(animationSpec = tween(500)),

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Generating response")
                Spacer(Modifier.width(20.dp))
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
fun ResponseCard(message: String, cardColor: Color, isLoading: Boolean) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .padding(20.dp)
                .background(cardColor),
            text = message, textAlign = TextAlign.Center,
            color = Color.White
        )


    }
}

@Composable
fun InputArea(placeHolder: String, inputState: TextFieldState, onSubmit: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.6f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                state = inputState,
                placeholder = { Text(placeHolder) },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(10.dp)
            )
            SubmitButton(onSubmit = { onSubmit() }, modifier = Modifier)
        }
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

private fun Modifier.isElementVisible(onVisibilityChanged: (Boolean) -> Unit) = composed {
    val isVisible by remember { derivedStateOf { mutableStateOf(false) } }
    LaunchedEffect(
        isVisible.value
    ) { onVisibilityChanged.invoke(isVisible.value) }
    this.onGloballyPositioned { layoutCoordinates ->
        isVisible.value = layoutCoordinates.parentLayoutCoordinates?.let {
            val parentBounds = it.boundsInWindow()
            val childBounds = layoutCoordinates.boundsInWindow()
            parentBounds.overlaps(childBounds)
        } == true
    }
}