package com.example.piano

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {

    private var _binding: FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val whiteKeys = listOf("C", "D", "E", "F", "G", "A", "B", "C", "D", "E", "F", "G", "A", "B")
    private val blackKeys = listOf("C#", "D#", "F#", "G#", "A#", "C#", "D#", "F#", "G#", "A#")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
       val view = binding.root

        val fragmentManager = childFragmentManager // Used to add fragments to this fragment
        val fragmentTransaction = fragmentManager.beginTransaction()    // Start fragment transaction, nothing happens until the changes are commited

        // For each note in whiteKeys,
        whiteKeys.forEach{
            var kp:keyPair
            if(blackKeys.contains("$it#"))
                kp = keyPair.newInstance("$it", "$it#")
            else
                kp = keyPair.newInstance("$it", "")

            fragmentTransaction.add(view.keys.id, kp, "note_$it")
        }

        fragmentTransaction.commit()

        return view // Return root view
    }

}