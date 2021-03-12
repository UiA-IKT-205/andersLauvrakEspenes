package com.example.piano

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.piano.data.NoteViewModel
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentKeyPairBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_key_pair.view.*


class keyPair : Fragment() {

    private var _binding:FragmentKeyPairBinding? = null
    private val binding get() = _binding!!
    private lateinit var fullNote:String
    private lateinit var halfNote:String

    private val viewModel: NoteViewModel by viewModels({requireParentFragment()})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fullNote = it.getString("FULLNOTE")?: "?"
            halfNote = it.getString("HALFNOTE")?: "?"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKeyPairBinding.inflate(layoutInflater)
        val view = binding.root
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // 1. Create and add white note
        // 2. If piano contains half key of note, create black key
        val whiteKey = Key.newInstance(fullNote)

        fragmentTransaction.add(view.keyPair.id, whiteKey, "note_$fullNote" )

        if(halfNote.contains("$fullNote#")) {
            val blackKey = Key.newInstance(halfNote)
            fragmentTransaction.add(view.keyPair.id, blackKey, "note_$halfNote")

        }
        fragmentTransaction.commit()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_key_pair, container, false)
    }

    companion object {
       @JvmStatic
        fun newInstance(fullNote: String, halfNote: String) =
                keyPair().apply {
                    arguments = Bundle().apply {
                        putString("FULLNOTE", fullNote)
                        putString("HALFNOTE", halfNote) // Can be null
                    }
                }
    }
}