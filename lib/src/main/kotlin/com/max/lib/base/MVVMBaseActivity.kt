/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.lib.base

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.max.lib.extension.showToast
import com.max.lib.utils.logger.MaxLogger
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import org.koin.android.ext.android.inject

abstract class MVVMBaseActivity<VB : ViewBinding> : FragmentActivity() {
    private lateinit var mBinding: VB
    val binding: VB
        get() = mBinding
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

    open fun askPermissions(
        permissions: List<String>,
        explainReason: String,
        explainReasonBeforeRequest: Boolean = false,
        forwardToSettingReason: String,
        callback: RequestCallback
    ) {
        val mediator = PermissionX.init(this@MVVMBaseActivity)
        val builder = mediator.permissions(permissions)

        builder.onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(deniedList, explainReason, "Okay", "Cancel")
        }

        builder.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(deniedList, forwardToSettingReason, "Okay", "Cancel")
        }

        if (explainReasonBeforeRequest) {
            builder.explainReasonBeforeRequest()
        }

        builder.request(callback)
    }

    open fun askSinglePermission(
        permissions: String,
        explainReason: String,
        explainReasonBeforeRequest: Boolean = false,
        forwardToSettingReason: String,
        callback: RequestCallback
    ) {
        val mediator = PermissionX.init(this@MVVMBaseActivity)
        val builder = mediator.permissions(permissions)

        builder.onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(deniedList, explainReason, "Okay", "Cancel")
        }

        builder.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(deniedList, forwardToSettingReason, "Okay", "Cancel")
        }

        if (explainReasonBeforeRequest) {
            builder.explainReasonBeforeRequest()
        }

        builder.request(callback)
    }

    fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askSinglePermission(
                Manifest.permission.POST_NOTIFICATIONS,
                "Reason",
                true,
                "Forward"
            ) { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    showToast("Permissions are Granted")
                    logger.e(grantedList.joinToString { "\n" })
                } else {
                    showToast("Permissions are denied")
                    logger.e(deniedList.joinToString { "\n" })
                }
            }
        }
    }
}