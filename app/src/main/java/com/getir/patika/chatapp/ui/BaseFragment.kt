package com.getir.patika.chatapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * A base fragment class providing support for view binding.
 *
 * @param T The type of view binding for this fragment.
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    /**
     * Inflates the layout and initializes the view binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    /**
     * Initializes the views.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /**
     * Retrieves the view binding instance.
     *
     * @param inflater The layout inflater.
     * @param container The container for the fragment.
     * @return The view binding instance.
     */
    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): T

    /**
     * Initializes the views.
     */
    protected abstract fun initializeViews()

    /**
     * Clears the view binding instance when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
