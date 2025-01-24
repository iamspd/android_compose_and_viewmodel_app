package com.example.scrambly.ui

data class GameUiState(
    val score: Int = 0,
    val currentWordCount: Int = 0,
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val hintCharacter: String = "",
    val isGameOver: Boolean = false
)
