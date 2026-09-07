package com.example.assignment_1_helloandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assignment_1_helloandroid.databinding.Fragment1Binding

// Key used to pass the selected button text between fragments.
internal const val ARG_SELECTED_BUTTON_TEXT = "selectedButton"

/**
 * Displays five buttons and navigates to [Fragment2] with the text
 * of the button selected by the user.
 */
class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = Fragment1Binding.inflate(inflater, container, false)

        val buttons = listOf(
            binding.firstButton,
            binding.secondButton,
            binding.thirdButton,
            binding.fourthButton,
            binding.fifthButton,
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                navigateToSecondFragment(button.text.toString())
            }
        }

        return binding.root
    }

    /**
     * Opens [Fragment2] and passes the selected button text to it.
     *
     * @param selectedButtonText the text displayed by the clicked button
     */
    private fun navigateToSecondFragment(selectedButtonText: String) {
        // Create an instance of Fragment2
        val fragment2 = Fragment2()
        val bundle = Bundle()
        bundle.putString(ARG_SELECTED_BUTTON_TEXT, selectedButtonText)
        fragment2.arguments = bundle

        // Replace the current fragment with Fragment2
        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment2)
            .addToBackStack(null).commit()
    }

}