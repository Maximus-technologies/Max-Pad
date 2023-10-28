/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.max.lib.base.MVVMBaseActivity
import com.max.main.databinding.ActivityMainBinding

class MainActivity : MVVMBaseActivity<ActivityMainBinding>() {

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
    }

}