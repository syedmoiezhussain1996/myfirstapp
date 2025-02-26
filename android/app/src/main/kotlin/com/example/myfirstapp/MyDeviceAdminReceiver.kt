package com.example.myfirstapp

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
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
    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        super.onProfileProvisioningComplete(context, intent)

        // 1. Notify the system provisioning is done
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponent = ComponentName(context, MyDeviceAdminReceiver::class.java)
        dpm.setProfileEnabled(adminComponent)

        // 2. Launch main activity
        val launchIntent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        context.startActivity(launchIntent)

        // 3. Enforce initial policies
        // dpm.setPasswordQuality(adminComponent, DevicePolicyManager.PASSWORD_QUALITY_NUMERIC)
        // dpm.setPasswordMinimumLength(adminComponent, 6)
    }
}
