package com.tuhoc.dreamtunes.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val messageLiveData = MutableLiveData<Int>()

    fun setMessage(id: Int) {
        messageLiveData.value = id
    }
}
