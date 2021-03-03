package com.example.piano

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.piano.databinding.FragmentKeyBinding
import kotlinx.android.synthetic.main.fragment_key.view.*


class Key : Fragment() {

    private var _binding:FragmentKeyBinding? = null // Note: FragmentWhiteKeyBinding is a automaticly generated class based on the xlm file fragment_white_key, name scheme is mandatory
    private val binding get() = _binding!!
    private lateinit var note:String // Contains the note this object will represent


    // Declare functions, must be defined by parent class
    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?" // Give default value since getString is nullable
        }
    }

    //@SuppressLint("ClickableViewAccessibility")
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
            var LP: Constraints.LayoutParams = Constraints.LayoutParams((52 * this.resources.displayMetrics.density).toInt(), (this.resources.displayMetrics.heightPixels/2))
            view.KeyButton.layoutParams = LP // Assign layout to button
            view.KeyButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.black) // Change button color
        }


        view.KeyButton.setOnTouchListener { _, event ->
            when (event?.action) {  // on touch event get touch event down and up and invoke function
                MotionEvent.ACTION_DOWN -> this@Key.onKeyDown?.invoke(note)
                MotionEvent.ACTION_UP -> this@Key.onKeyUp?.invoke(note)
            }
            true
        }


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
}