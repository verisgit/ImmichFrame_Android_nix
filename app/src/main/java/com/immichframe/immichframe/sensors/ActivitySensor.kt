package com.immichframe.immichframe.sensors

import android.content.Context
import android.util.Log
import com.kitesystems.nix.frame.MotionSensor
import java.io.File

/**
 * Manages motion-based screen sleep/wake for Nixplay frames.
 *
 * On a Nixplay device (/etc/nix.model present), uses the hardware GPIO
 * motion sensor via MotionSensor.java + libgpio_jni.so.
 *
 * On non-Nixplay devices, falls back to a sensor that always reports
 * activity (no-op behaviour — screen never sleeps via this path).
 */
class ActivitySensor(context: Context, activitySensorTimeout: Int) {
    private val poller: SensorPoller
    private val powerUtils: PowerUtils = PowerUtils(context)

    init {
        val hardwareSensor: HardwareSensor = if (File("/etc/nix.model").exists()) {
            Log.i("ActivitySensor", "Nixplay device detected — using hardware motion sensor")
            MotionSensor()
        } else {
            Log.i("ActivitySensor", "Not a Nixplay device — motion sensor inactive")
            HardwareSensor { true }
        }
        poller = SensorPoller(hardwareSensor, activitySensorTimeout)
    }

    private val sensorCallback = object : SensorServiceCallback {
        override fun sleep() {
            Log.d("ActivitySensor", "No motion — releasing wake lock")
            powerUtils.goToSleep()
        }

        override fun wakeUp() {
            Log.d("ActivitySensor", "Motion detected — reacquiring wake lock")
            poller.resetMotionSensor()
            powerUtils.wakeUp()
        }
    }

    fun checkSensors() {
        try {
            Thread.currentThread().priority = Thread.MAX_PRIORITY
            poller.checkIfActivityDetected(sensorCallback)
        } catch (e: SecurityException) {
            Log.e("ActivitySensor", e.toString())
        }
    }

    fun release() {
        powerUtils.release()
    }
}