package com.immichframe.immichframe.sensors

fun interface HardwareSensor {
    fun isActivityDetected(): Boolean
}