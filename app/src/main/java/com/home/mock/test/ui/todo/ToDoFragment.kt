package com.home.mock.test.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.home.mock.test.databinding.FragmentToDoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoFragment : Fragment() {

    // Binding variable
    private var _binding: FragmentToDoBinding? = null
    private val binding: FragmentToDoBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout using the binding object
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root // Return the root view of the fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition() // Postpone the transition until the views are ready
        view.doOnPreDraw { startPostponedEnterTransition() } // Start the postponed transition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Release the binding reference
    }
}
