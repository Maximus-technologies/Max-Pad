package com.max.lib.extension

import android.app.Activity
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.max.lib.base.MVVMBaseActivity
import com.max.lib.base.MVVMBaseViewModel

inline fun <reified VB : ViewDataBinding> Activity.viewDataBindingOf(): VB {
    return DataBindingUtil.findBinding((findViewById<ViewGroup>(android.R.id.content)).getChildAt(0))!!
}

fun FragmentActivity.replaceFragment(id: Int, fragment: Fragment, tag: String? = null) {
    val fm = supportFragmentManager
    val ft = fm.beginTransaction()
    ft.replace(id, fragment, tag)
    ft.commit()
}

fun MVVMBaseActivity<*>.bindBaseLiveData(vm: MVVMBaseViewModel) {
    vm.toastLiveData.observe(this, Observer { showToast(it) })
}