package com.max.lib.base

import androidx.lifecycle.ViewModel
import com.max.lib.livedata.SingleLiveEvent

abstract class MVVMBaseViewModel : ViewModel(), MVVMLifecycle {

    val toastLiveData = SingleLiveEvent<String>()

    fun showToast(msg: String?) {
        toastLiveData.postValue(msg!!)
    }

    /**
     * Lifecycle Start
     */
    override fun onCreate() {}

    override fun onPause() {}

    override fun onResume() {}

    override fun onDestroy() {}
    /**
     * Lifecycle End
     */
}