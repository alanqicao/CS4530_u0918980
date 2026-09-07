package com.example.assignment_1_helloandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignment_1_helloandroid.databinding.Fragment2Binding


/**
 * Displays the button text received from [Fragment1] and allows
 * the user to return to the previous fragment.
 */
class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = Fragment2Binding.inflate(inflater, container, false)

        val selectedButtonText = arguments?.getString(ARG_SELECTED_BUTTON_TEXT)
        binding.textView2.text = selectedButtonText

        // return button
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }


}