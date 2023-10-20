package com.max.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.max.lib.utils.logger.MaxLogger
import org.koin.android.ext.android.inject

abstract class MVVMBaseActivity<VB : ViewBinding> : FragmentActivity() {
    private lateinit var mBinding: VB
    val logger: MaxLogger by inject()

    abstract fun setBinding(layoutInflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBinding(layoutInflater)
        setContentView(mBinding.root)
    }
}