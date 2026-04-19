package com.immichframe.immichframe.sensors

import android.content.Context
import android.os.PowerManager

class PowerUtils(context: Context) {
    @Suppress("DEPRECATION")
    private val wakeLock: PowerManager.WakeLock = context
        .getSystemService(PowerManager::class.java)
        .newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "ImmichFrame:MotionSensor"
        )

    init {
        wakeUp()
    }

    fun goToSleep() {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    fun wakeUp() {
        if (!wakeLock.isHeld) {
            @Suppress("DEPRECATION")
            wakeLock.acquire(10 * 60 * 1000L)
        }
    }

    fun release() {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}