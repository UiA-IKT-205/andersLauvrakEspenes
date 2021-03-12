package com.example.piano

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.piano.data.Note
import com.example.piano.data.NoteViewModel
import com.example.piano.databinding.FragmentKeyBinding
import kotlinx.android.synthetic.main.fragment_key.view.*


class Key : Fragment() {

    private var _binding:FragmentKeyBinding? = null // Note: FragmentWhiteKeyBinding is a automaticly generated class based on the xlm file fragment_white_key, name scheme is mandatory
    private val binding get() = _binding!!
    private lateinit var note:String // Contains the note this object will represent

    private val viewModel: NoteViewModel by viewModels({requireParentFragment().requireParentFragment()})
    var startTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?" // Give default value since getString is nullable
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKeyBinding.inflate(inflater, container, false)
        val view = binding.root

        if(note.contains("#")) {
            // Create new layout for black key
            // Calculate height of black keys depending on screen size, width is more fixed by 52 integer converted to dp
            val layoutParams: Constraints.LayoutParams = Constraints.LayoutParams((52 * this.resources.displayMetrics.density).toInt(), (this.resources.displayMetrics.heightPixels/2))
            view.KeyButton.layoutParams = layoutParams // Assign layout to button
            view.KeyButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.black) // Change button color
        }

        view.KeyButton.setOnTouchListener(object: View.OnTouchListener {

            @SuppressLint("ClickableViewAccessibility") // Dont' want click sound from performClick
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action) {
                    MotionEvent.ACTION_DOWN -> this@Key.onKeyDown(note)
                    MotionEvent.ACTION_UP -> this@Key.onKeyUp(note)
                }
                return true
            }
        })
        // Inflate the layout for this fragment
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            Key().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }

    private fun onKeyDown(note: String){
        Log.d("Key Down", "$note key down")
        startTime = System.nanoTime() // Get OS time when key pressed
    }

    private fun onKeyUp(note: String) {
        Log.d("Key Up", "$note key up")
        if(viewModel.isEmpty()) // Store the OS time of the first note
            viewModel.setStartTime(startTime)
        val temp = Note(note, startTime - viewModel.getStartTime(), System.nanoTime() - startTime)
        viewModel.addNote(temp)
    }
}
