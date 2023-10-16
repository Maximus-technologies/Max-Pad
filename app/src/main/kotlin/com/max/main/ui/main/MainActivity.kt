package com.max.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.max.main.databinding.ActivityMainBinding
import com.max.lib.base.MVVMBaseActivity

class MainActivity : MVVMBaseActivity<ActivityMainBinding>() {

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}