import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.max.firebase.BuildConfig
import com.max.lib.data.local.MaxLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.resume

class RemoteConfigManager(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : KoinComponent {
    private val prefs: MaxLocal by inject()
    private val gson: Gson by inject()
    private val timeInMillis: Long = if (BuildConfig.DEBUG) 0L else 3600L

    private val configSettings = FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(timeInMillis) // Set your desired cache expiration time
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
                                } catch (e: JsonParseException) {
                                    // Handle JSON parsing error
                                    val localConfig = getLocalConfig()
                                    if (localConfig != null) {
                                        cont.resume(Result.success(localConfig))
                                    } else {
                                        cont.resume(Result.failure(Exception("Failed to parse remote config JSON")))
                                    }
                                }
                            } else {
                                val localConfig = getLocalConfig()
                                if (localConfig != null) {
                                    cont.resume(Result.success(localConfig))
                                } else {
                                    cont.resume(Result.failure(Exception("Failed to fetch remote config")))
                                }
                            }
                        }
                    } else {
                        val localConfig = getLocalConfig()
                        if (localConfig != null) {
                            cont.resume(Result.success(localConfig))
                        } else {
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

data class RemoteConfig(val key1: KeyValue, val key2: KeyValue)

data class KeyValue(val value: String)