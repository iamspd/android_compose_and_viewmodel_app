package com.example.scrambly.data

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

// set of all words for the test
val wordBank: Set<String> = setOf(
    "at",
    "sea",
    "home",
    "arise",
    "banana",
    "android",
    "birthday",
    "briefcase",
    "motorcycle",
    "cauliflower"
)

/**
 * Maps words to their lengths. Each word in wordBank has a unique length.
 * This is done since the words are randomly picked inside GameViewModel and the
 *      selection is unpredictable.
 */
private val wordLengthMap: Map<Int, String> = wordBank.associateBy({ it.length }, { it })
internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""