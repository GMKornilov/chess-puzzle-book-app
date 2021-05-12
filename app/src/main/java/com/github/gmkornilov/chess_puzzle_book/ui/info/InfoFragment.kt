package com.github.gmkornilov.chess_puzzle_book.ui.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentInfoBinding
import com.google.android.material.snackbar.Snackbar

class InfoFragment : Fragment() {
    private val infoViewModel: InfoViewModel by viewModels {
        InfoViewModelFactory(resources)
    }
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_info,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = infoViewModel

        infoViewModel.loadedEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                if (it.error.isNotEmpty()) {
                    Snackbar.make(requireView(), it.error, Snackbar.LENGTH_SHORT).show()
                } else {
                    Log.d("Loaded tasks", it.result.toString())
                }
            }
        })
        infoViewModel.exceptionEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}