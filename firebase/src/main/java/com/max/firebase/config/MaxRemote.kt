/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.firebase.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.max.firebase.BuildConfig
import com.max.lib.data.local.MaxLocal
import com.max.lib.utils.logger.MaxLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume

class RemoteConfigManager(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : KoinComponent {
    private val prefs: MaxLocal by inject()
    private val gson: Gson by inject()
    private val timeInMillis: Long = if (BuildConfig.DEBUG) 0L else 3600L
    private val logger: MaxLogger by inject()
    private val TAG = "max_remote_config"

    private val configSettings = FirebaseRemoteConfigSettings.Builder()
//        .setMinimumFetchIntervalInSeconds(timeInMillis) // Set your desired cache expiration time
//        .setFetchTimeoutInSeconds(timeInMillis * 4)
        .build()

    suspend fun fetchRemoteConfig(): Result<RemoteConfig> = withContext(Dispatchers.IO) {
        return@withContext suspendCancellableCoroutine { cont ->
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { fetchTask ->
                            if (fetchTask.isSuccessful) {
                                val remoteConfigJson =
                                    firebaseRemoteConfig.getString(BuildConfig.remote_key)
                                try {
                                    val remoteConfig =
                                        gson.fromJson(remoteConfigJson, RemoteConfig::class.java)
                                    // Cache the remote config in SharedPreferences
                                    prefs.putString("remote_config", remoteConfigJson)
                                    cont.resume(Result.success(remoteConfig))
                                    logger.i(TAG, "Remote Success")
                                } catch (e: JsonParseException) {
                                    // Handle JSON parsing error
                                    val localConfig = getLocalConfig()
                                    if (localConfig != null) {
                                        logger.i(
                                            TAG,
                                            "Resuming with local on JSON Parsing Exception"
                                        )
                                        cont.resume(Result.success(localConfig))
                                    } else {
                                        logger.i(TAG, "Failed to parse Remote config JSON")
                                        cont.resume(Result.failure(Exception("Failed to parse remote config JSON")))
                                    }
                                }
                            } else {
                                val localConfig = getLocalConfig()
                                if (localConfig != null) {
                                    logger.i(TAG, "Remote Fetch Failed but Resuming with Local")
                                    cont.resume(Result.success(localConfig))
                                } else {
                                    logger.i(TAG, "Remote Fetch Failed with No Data.Resuming")
                                    cont.resume(Result.failure(Exception("Failed to fetch remote config")))
                                }

                            }
                        }
                    } else {
                        val localConfig = getLocalConfig()
                        if (localConfig != null) {
                            logger.i(TAG, "Getting Old remote configurations")
                            cont.resume(Result.success(localConfig))
                        } else {
                            logger.i(TAG, "No Options. Everything failed. Continuing")
                            cont.resume(Result.failure(Exception("Failed to set config settings")))
                        }
                    }
                }
        }
    }

    private fun getLocalConfig(): RemoteConfig? {
        val jsonString = prefs.getString("remote_config", null)
        return jsonString?.let { gson.fromJson(it, RemoteConfig::class.java) }
    }

    companion object {
        @Volatile
        private var INSTANCE: RemoteConfigManager? = null

        fun getInstance(
            firebaseRemoteConfig: FirebaseRemoteConfig
        ): RemoteConfigManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RemoteConfigManager(firebaseRemoteConfig)
                    .also { INSTANCE = it }
            }
        }
    }
}

data class KeyValue(val value: String)