package com.max.lib.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class MVVMBaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId)