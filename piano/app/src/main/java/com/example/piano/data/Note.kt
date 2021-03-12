package com.example.piano.data

data class Note(
        val note: String,               // The note character
        val noteStart: Long,            // When to start the note, first note is 0
        val pressedTime: Long           // OS time key was held
        )

{
    override fun toString(): String {
        return "$note,$noteStart,$pressedTime\n"
    }
}
