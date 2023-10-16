package com.max.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

abstract class MVVMBaseActivity<VB : ViewBinding> : FragmentActivity() {
    private lateinit var mBinding: VB

    abstract fun setBinding(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBinding(layoutInflater)
        setContentView(mBinding.root)
    }
}