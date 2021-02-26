package com.example.piano

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.piano.databinding.FragmentWhiteKeyBinding
import kotlinx.android.synthetic.main.fragment_white_key.view.*


class WhiteKey : Fragment() {

    private var _binding:FragmentWhiteKeyBinding? = null // Note: FragmentWhiteKeyBinding is a automaticly generated class based on the xlm file fragment_white_key, name scheme is mandatory
    private val binding get() = _binding!!
    private lateinit var note:String // Contains the note this object will represent
    private var black: Boolean = false// True if key should be black


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
        _binding = FragmentWhiteKeyBinding.inflate(inflater, container, false)
        val view = binding.root

        if(black)
            view.whiteKeyButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.black)

        view.whiteKeyButton.setOnTouchListener { _, event ->
            when (event?.action) {  // on touch event get touch event down and up and invoke function
                MotionEvent.ACTION_DOWN -> this@WhiteKey.onKeyDown?.invoke(note)
                MotionEvent.ACTION_UP -> this@WhiteKey.onKeyUp?.invoke(note)
            }
            true
        }


        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            WhiteKey().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                    if(note.contains("#")) {
                        black = true
                    }

                }
            }
    }
}