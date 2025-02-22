package com.example.myfirstapp

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import io.flutter.plugin.common.MethodChannel

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    companion object {
        var methodChannel: MethodChannel? = null
    }
    
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "Device Admin Enabled", Toast.LENGTH_SHORT).show()
         methodChannel?.invokeMethod("onAdminStatusChanged", true) // Notify Flutter
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show()
          methodChannel?.invokeMethod("onAdminStatusChanged", false) // Notify Flutter
    }

    override fun onPasswordFailed(context: Context, intent: Intent) {
        Toast.makeText(context, "Password attempt failed!", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent) {
        Toast.makeText(context, "Password entered correctly", Toast.LENGTH_SHORT).show()
    }
}
