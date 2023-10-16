package com.max.lib.extension

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.max.lib.base.MVVMBaseFragment
import com.max.lib.base.MVVMBaseViewModel

inline fun <reified VB : ViewDataBinding> Fragment.viewDataBindingOf(): VB {
    return DataBindingUtil.bind(view!!)!!
}

fun MVVMBaseFragment.bindBaseLiveData(vm: MVVMBaseViewModel) {
    vm.toastLiveData.observe(this, Observer { context?.showToast(it) })
}