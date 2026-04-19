package com.kitesystems.nix.frame;

import android.util.Log;

import com.immichframe.immichframe.sensors.HardwareSensor;

/**
 * JNI wrapper for the Nixplay frame's hardware motion sensor (GPIO).
 *
 * The package name com.kitesystems.nix.frame must match the JNI symbol names
 * registered in /system/lib/libgpio_jni.so on the Nixplay device.
 *
 * On non-Nixplay devices (or if the library fails to load), LIBRARY_LOADED
 * stays false and isActivityDetected() returns false — but ActivitySensor
 * only instantiates this class when /etc/nix.model is present, so that
 * case is never reached in practice.
 */
public class MotionSensor implements HardwareSensor {
    private static boolean LIBRARY_LOADED;
    private static final String LIBRARY_NAME = "gpio_jni";

    public native int readMotionSensor();

    public native boolean readMotionSensorPower();

    public native void setMotionSensorPower(boolean b);

    public native int setWakeOnMotion(boolean b);

    @Override
    public synchronized boolean isActivityDetected() {
        if (LIBRARY_LOADED) {
            if (!readMotionSensorPower()) {
                setMotionSensorPower(true);
            }
            return readMotionSensor() > 0;
        }
        return false;
    }

    static {
        LIBRARY_LOADED = false;
        try {
            System.loadLibrary(LIBRARY_NAME);
            LIBRARY_LOADED = true;
        } catch (UnsatisfiedLinkError e) {
            Log.i("MotionSensor", String.format(
                "native library %s could not be loaded: %s", LIBRARY_NAME, e.getMessage()));
        }
    }
}