package com.example.piano

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {

    private var _binding: FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val whiteKeys = listOf("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G", "A", "B")
    private val blackKeys = listOf("C#", "D#", "F#", "G#", "A#", "C#", "D#", "F#", "G#", "A#")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
       val view = binding.root

        val fragmentManager = childFragmentManager // Used to add fragments to this fragment
        val fragmentTransaction = fragmentManager.beginTransaction()    // Start fragment transaction, nothing happens until the changes are commited

        whiteKeys.forEach{ it -> // Create a key button for each note
            val key = WhiteKey.newInstance(it) // Create a full tone key (white key)

            // Create the onKeyDown and onKeyUp here, in order to get same behavior from every type of key
            key.onKeyDown = {
                Log.d("Key down", "$it white key down")

            }

            key.onKeyUp = {
                Log.d("Key up", "$it white key up")
            }

            fragmentTransaction.add(view.whiteKeys.id, key, "note_$it") // Add the key to the horizontal view
        }

        blackKeys.forEach { it -> // Create a key button for each note
            val key = WhiteKey.newInstance(it) // Create a full tone key (white key)

            // Create the onKeyDown and onKeyUp here, in order to get same behavior from every type of key
            key.onKeyDown = {
                Log.d("Key down", "$it black key down")

            }

            key.onKeyUp = {
                Log.d("Key up", "$it black key up")
            }

            fragmentTransaction.add(view.blackKeys.id, key, "note_$it") // Add the key to the horizontal view
        }

        fragmentTransaction.commit()

        return view // Return root view
    }

}