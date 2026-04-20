package com.immichframe.immichframe.sensors

import android.content.Context
import android.os.PowerManager

class PowerUtils(context: Context) {
    private val powerManager = context.getSystemService(PowerManager::class.java)

    // Keeps the screen bright and forces it on when acquired
    @Suppress("DEPRECATION")
    private val screenWakeLock: PowerManager.WakeLock = powerManager.newWakeLock(
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
        "ImmichFrame:MotionSensorScreen"
    )

    // Keeps the CPU running even when the screen is off so sensor polling continues
    private val cpuWakeLock: PowerManager.WakeLock = powerManager.newWakeLock(
        PowerManager.PARTIAL_WAKE_LOCK,
        "ImmichFrame:MotionSensorCPU"
    )

    init {
        cpuWakeLock.acquire(12 * 60 * 60 * 1000L) // 12 hours
        wakeUp()
    }

    fun goToSleep() {
        if (screenWakeLock.isHeld) {
            screenWakeLock.release()
        }
    }

    fun wakeUp() {
        if (!screenWakeLock.isHeld) {
            @Suppress("DEPRECATION")
            screenWakeLock.acquire(10 * 60 * 1000L)
        }
    }

    fun release() {
        if (screenWakeLock.isHeld) {
            screenWakeLock.release()
        }
        if (cpuWakeLock.isHeld) {
            cpuWakeLock.release()
        }
    }
}