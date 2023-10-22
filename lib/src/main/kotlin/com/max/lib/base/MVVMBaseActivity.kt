/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

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

    open fun runBeforeCreating() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        runBeforeCreating()
        super.onCreate(savedInstanceState)
        mBinding = setBinding(layoutInflater)
        setContentView(mBinding.root)
    }
}