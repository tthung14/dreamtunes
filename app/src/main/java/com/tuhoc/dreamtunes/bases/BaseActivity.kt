package com.tuhoc.dreamtunes.bases

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(private val bindingInflater: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    val binding by lazy { bindingInflater(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observerData()
        initData()
        initView()
        handleEvent()
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