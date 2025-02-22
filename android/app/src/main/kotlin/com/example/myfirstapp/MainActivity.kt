package com.example.myfirstapp

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "device_admin"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        MyDeviceAdminReceiver.methodChannel = methodChannel // Set the channel for the receiver

        methodChannel.setMethodCallHandler { call, result ->
            val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)

            when (call.method) {
                "activateDeviceAdmin" -> {
                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                        putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                        putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable device admin to protect your data")
                    }
                    startActivity(intent)
                    result.success("Device Admin Activation Started")
                }

                "deactivateDeviceAdmin" -> {
                    if (devicePolicyManager.isAdminActive(componentName)) {
                        devicePolicyManager.removeActiveAdmin(componentName)
                        result.success("Device Admin Deactivated")
                    } else {
                        result.error("NOT_ADMIN", "Device Admin is not active", null)
                    }
                }

                "isDeviceAdminActive" -> {
                    val isActive = devicePolicyManager.isAdminActive(componentName)
                    result.success(isActive)
                }

                else -> result.notImplemented()
            }
        }
    }
}
