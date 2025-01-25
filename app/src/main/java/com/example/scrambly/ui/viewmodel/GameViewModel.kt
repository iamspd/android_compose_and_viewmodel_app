package com.example.scrambly.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scrambly.data.MAX_NO_OF_WORDS
import com.example.scrambly.data.SCORE_INCREASE
import com.example.scrambly.data.wordBank
import com.example.scrambly.ui.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _gameUiState = MutableStateFlow(GameUiState())
    val gameUiState: StateFlow<GameUiState> = _gameUiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = wordBank.random()

        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val charArrayOfWord = word.toCharArray()
        charArrayOfWord.shuffle()

        while (String(charArrayOfWord) == currentWord) {
            charArrayOfWord.shuffle()
        }
        return String(charArrayOfWord)
    }

    fun updateUserGuess(guessWord: String) {
        userGuess = guessWord
    }

    /**
     * this is called when the submit button is pressed
     * check if the user guessed the correct word and update the
     *      game stats accordingly
     */
    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _gameUiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _gameUiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = true
                )
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(score: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _gameUiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = score,
                    isHintRequested = false,
                    isGuessedWordWrong = false
                )
            }
        } else {
            _gameUiState.update { currentState ->
                currentState.copy(
                    isGameOver = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = score,
                    isHintRequested = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    isGuessedWordWrong = false
                )
            }
        }
    }

    fun skipWord() {
        updateGameState(_gameUiState.value.score)
        // Reset the user guess
        updateUserGuess("")
    }

    /**
     * update the GameUiState and set the isHintRequested flag to true
     */
    fun requestHint() {
        _gameUiState.update { currentState ->
            currentState.copy(
                isHintRequested = true
            )
        }
    }

    /**
     * show first character of the word as a string on hint text
     */
    fun showHintChar(): String {
        return currentWord.substring(startIndex = 0, endIndex = 1)
    }


    /**
     * call this method when the game is launched for the first time,
     * and the user wants to play again.
     */

    fun resetGame() {
        usedWords.clear()
        _gameUiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }
}