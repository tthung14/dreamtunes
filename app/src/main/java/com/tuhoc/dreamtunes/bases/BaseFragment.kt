package com.tuhoc.dreamtunes.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflate: (LayoutInflater) -> VB
) : Fragment() {

    val binding by lazy { bindingInflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observerData()
        initView()
        handleEvent()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val navController = findNavController()
                    if (navController.currentDestination?.id == navController.graph.startDestinationId) {
                        requireActivity().finishAndRemoveTask()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
        return binding.root
    }

    open fun initData() {

    }

    open fun initView() {

    }

    open fun handleEvent() {

    }

    open fun observerData() {

    }

}