package com.example.piano

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.piano.data.NoteViewModel
import com.example.piano.databinding.FragmentPianoBinding
import com.example.piano.services.FirebaseService
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.lang.NullPointerException


class PianoLayout : Fragment() {

    private val TAG: String = "PianoLayout"

    private var _binding: FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val whiteKeys =
        listOf("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G", "A", "B")
    private val blackKeys = listOf("C#", "D#", "F#", "G#", "A#", "C#", "D#", "F#", "G#", "A#")

    private val viewModel: NoteViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager // Used to add fragments to this fragment
        val fragmentTransaction =
            fragmentManager.beginTransaction()    // Start fragment transaction, nothing happens until the changes are commited

        // For each note in whiteKeys,
        whiteKeys.forEach {
            val kp: keyPair = if (blackKeys.contains("$it#"))
                keyPair.newInstance(it, "$it#")
            else
                keyPair.newInstance(it, "")

            fragmentTransaction.add(view.keys.id, kp, "note_$it")
        }

        fragmentTransaction.commit()

        // Save score on save button click
        view.saveScoreButton.setOnClickListener {
            Log.d("Layout", viewModel.toString())
            val fileName = view.fileNameTextEdit.text.toString()
            if (viewModel.isNotEmpty()) {    // Check if there is a score to save
                if (validateFilename(fileName)) { // Check if fileName input is valid
                    try {
                        saveScore(fileName)
                    } catch (e: NullPointerException) {
                        Toast.makeText(activity, "Save path is invalid: $e", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else Toast.makeText(activity, "Filename is invalid", Toast.LENGTH_SHORT).show()

            } else Toast.makeText(activity, "Create a song to save", Toast.LENGTH_SHORT).show()
        }

        return view // Return root view
    }

    private fun validateFilename(fileName: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return when {
            fileName.isEmpty() -> false
            fileName.length > 40 -> false
            !regex.matches(fileName) -> false
            else -> true
        }
    }

    private fun saveScore(prefix: String) {
        val suffix = ".score"
        val fileName = prefix + suffix
        this.context?.let { viewModel.save(it, fileName) }
    }

    private fun save(context: Context, fileName: String) {
        Intent(context, FirebaseService::class.java).also {
            Log.i(TAG, viewModel.toString())
            it.putExtra("FILENAME", fileName)
            it.putExtra("SCORE", toString())
            context.startService(it)
        }
    }
}