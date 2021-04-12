package com.example.piano.data

import androidx.lifecycle.ViewModel

// TODO: Add LiveData support for view model, to allow for obeservers to automaticly update upon changes
class NoteViewModel : ViewModel() {
    private val mutableScore = mutableListOf<Note>()
    private var firstNoteTime: Long = 0

    // Returns the score
    fun getScore(): List<Note> {
        return mutableScore
    }
    // Converts List to one string
    override fun toString(): String {
        return mutableScore.map { it.toString() }.reduce{acc: String, s: String -> acc + s }
    }

    // Adds note to the score
    fun addNote(note: Note) {
        mutableScore.add(note)
    }

    // Check if there is a note
    fun isNotEmpty(): Boolean {
        return mutableScore.isNotEmpty()
    }

    // Check if there is NOT a note
    fun isEmpty(): Boolean {
        return mutableScore.isEmpty()
    }

    // Returns the OS time when the first note was played
    fun getStartTime(): Long {
        return firstNoteTime
    }

    fun setStartTime(startTime: Long){
        firstNoteTime = startTime
    }

}