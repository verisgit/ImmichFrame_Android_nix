package com.immichframe.immichframe.sensors

import android.util.Log

class SensorPoller(
    private val sensor: HardwareSensor,
    timeoutMinutes: Int
) {
    @Volatile private var sleepThresholdMs: Long = timeoutMinutes * 60 * 1000L
    private var idleStartTime: Long = System.currentTimeMillis()
    @Volatile private var isSleeping: Boolean = false
    private var lastLogTime: Long = 0L

    fun checkIfActivityDetected(callback: SensorServiceCallback) {
        if (sensor.isActivityDetected()) {
            if (isSleeping) {
                callback.wakeUp()
            }
            idleStartTime = System.currentTimeMillis()
        } else {
            val now = System.currentTimeMillis()
            val idleDuration = now - idleStartTime
            if (now - lastLogTime > 30_000L) {
                Log.d("SensorPoller", "No activity. Idle ${idleDuration / 1000}s / threshold ${sleepThresholdMs / 1000}s")
                lastLogTime = now
            }
            if (!isSleeping && idleDuration >= sleepThresholdMs) {
                isSleeping = true
                callback.sleep()
            }
        }
    }

    @Synchronized
    fun updateSettings(timeoutMinutes: Int) {
        sleepThresholdMs = timeoutMinutes * 60 * 1000L
    }

    fun resetMotionSensor() {
        idleStartTime = System.currentTimeMillis()
        isSleeping = false
    }

    fun isSleepModeActive(): Boolean = isSleeping
}