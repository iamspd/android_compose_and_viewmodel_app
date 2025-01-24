package com.example.scrambly.ui.test

import com.example.scrambly.data.MAX_NO_OF_WORDS
import com.example.scrambly.data.SCORE_INCREASE
import com.example.scrambly.data.getUnscrambledWord
import com.example.scrambly.ui.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val gameViewModel = GameViewModel()

    // test case for happy path
    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = gameViewModel.gameUiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        gameViewModel.updateUserGuess(correctPlayerWord)
        gameViewModel.checkUserGuess()

        currentGameUiState = gameViewModel.gameUiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    // test case for error path
    @Test
    fun gameViewModel_IncorrectWordGuessed_ErrorFlagSetAndScoreNotUpdated() {
        val incorrectPayerWord = "well"
        gameViewModel.updateUserGuess(incorrectPayerWord)
        gameViewModel.checkUserGuess()

        val currentGameUiState = gameViewModel.gameUiState.value
        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(INITIAL_SCORE, currentGameUiState.score)
    }

    // test case when app is launched
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val currentGameUiState = gameViewModel.gameUiState.value
        val unscrambledWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        assertNotEquals(unscrambledWord, currentGameUiState.currentScrambledWord)
        assertTrue(currentGameUiState.currentWordCount == 1)
        assertTrue(currentGameUiState.score == 0)
        assertFalse(currentGameUiState.isHintRequested)
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertFalse(currentGameUiState.isGameOver)
    }

    // test case when game is over with all correct words guessed
    @Test
    fun gameViewModel_AllWordsGuessedCorrectly_UIStateUpdatedCorrectly() {
        var currentGameUiState = gameViewModel.gameUiState.value
        var expectedScore = 0
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            gameViewModel.updateUserGuess(correctPlayerWord)
            gameViewModel.checkUserGuess()
            currentGameUiState = gameViewModel.gameUiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
        }

        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    // test case for the behavior of skip button
    @Test
    fun gameViewModel_WordSkipped_WordCountIncreasedAndScoreIsUnchanged() {
        var currentGameUiState = gameViewModel.gameUiState.value
        val wordCount = 1

        gameViewModel.skipWord()
        currentGameUiState = gameViewModel.gameUiState.value
        assertEquals(wordCount.inc(), currentGameUiState.currentWordCount)
        assertEquals(INITIAL_SCORE, currentGameUiState.score)
    }

    // test case when the player requests a hint
    @Test
    fun gameViewModel_PlayerRequestedAHint_HintFlagIsSetAndFirstLetterIsDisplayed() {
        var currentGameUiState = gameViewModel.gameUiState.value
        val unscrambledWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        val hintCharacter = unscrambledWord.substring(startIndex = 0, endIndex = 1)

        gameViewModel.requestHint()
        currentGameUiState = gameViewModel.gameUiState.value
        assertTrue(currentGameUiState.isHintRequested)
        assertEquals(hintCharacter, gameViewModel.showHintChar())
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = 20
        private const val INITIAL_SCORE = 0
    }
}