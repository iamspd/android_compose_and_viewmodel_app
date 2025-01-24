package com.example.scrambly.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrambly.R
import com.example.scrambly.ui.theme.ScramblyTheme
import com.example.scrambly.ui.theme.cosmic
import com.example.scrambly.ui.theme.sky_blue
import com.example.scrambly.ui.viewmodel.GameViewModel

@Preview(showBackground = true)
@Composable
fun PreviewGameScreen() {
    ScramblyTheme {
        GameScreen()
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {

    val gameUiState by gameViewModel.gameUiState.collectAsState()

    Column(
        modifier = modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .fillMaxSize()
            .padding(12.dp)
    ) {
        ScoreCard(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 18.dp),
            score = gameUiState.score
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
            )

            GameCard(
                modifier = Modifier,
                wordCount = gameUiState.currentWordCount,
                currentScrambledWord = gameUiState.currentScrambledWord,
                userGuess = gameViewModel.userGuess,
                onUserGuessChange = { gameViewModel.updateUserGuess(it) },
                isGuessWrong = gameUiState.isGuessedWordWrong,
                isHintRequested = gameUiState.isHintRequested,
                showHint = { gameViewModel.showHint() },
                hint = gameViewModel.showHintChar(),
                onKeyboardDone = { gameViewModel.checkUserGuess() }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
                onClick = { gameViewModel.checkUserGuess() }
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { gameViewModel.skipWord() }
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    wordCount: Int,
    currentScrambledWord: String,
    userGuess: String,
    onUserGuessChange: (String) -> Unit,
    isGuessWrong: Boolean,
    isHintRequested: Boolean,
    showHint: () -> Unit,
    hint: String,
    onKeyboardDone: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showHint() },
                    painter = painterResource(R.drawable.icon_hint),
                    contentDescription = stringResource(R.string.hint_icon_description),
                )

                Text(
                    text = stringResource(R.string.word_count, wordCount),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(sky_blue)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .align(Alignment.CenterVertically),
                    color = cosmic
                )
            }

            Text(
                text = currentScrambledWord,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )

            Text(
                text = if (isHintRequested) {
                    stringResource(R.string.show_hint, hint)
                } else {
                    stringResource(R.string.unscrmable_instruction)
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = userGuess,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onUserGuessChange,
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                label = {
                    Text(
                        text = stringResource(R.string.guess_the_word),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                isError = isGuessWrong,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )
        }
    }
}

@Composable
fun ScoreCard(
    modifier: Modifier = Modifier,
    score: Int
) {
    Card(modifier = modifier) {
        Text(
            text = stringResource(R.string.score, score),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


