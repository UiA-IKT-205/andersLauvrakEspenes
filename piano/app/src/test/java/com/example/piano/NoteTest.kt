package com.example.piano

import com.example.piano.data.Note
import org.junit.Test
import org.junit.Assert.*

class NoteTest {

    @Test
    fun createNoteTest() {
        val note = Note("C", 0, 1)
        assertEquals("C,0,1\n", note.toString())
    }
}