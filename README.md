# ImmichFrame Android (Nixplay)

An Android photo frame app for Nixplay frames, based on [ImmichFrame Android](https://github.com/immichFrame/ImmichFrame_Android).

Adds motion sensor support so the frame screen sleeps when no one is around and wakes when movement is detected.

## Download

Download the latest APK from Releases.

## Installation

You must jailbreak your Nixplay frame to install the app and uninstall the native nixplay app. See guide on Youtube https://www.youtube.com/watch?v=TN5errM5UbA

The app must be installed as a **system app** so it can access the Nixplay hardware motion sensor library. This requires the frame to be rooted.

1. Connect your Nixplay frame via ADB
2. If you had the normal immichframe app installed, you'd need to uninstall it first.

```bash
adb uninstall com.immichframe.immichframe
```

3. Run the following commands:

```bash
adb root && adb remount && adb push immichframe.apk /system/app/immichframe.apk
adb reboot
```

After reboot the app will appear as the launcher.

## Setup

1. Open the app — It will take you to Settings on first launch. Using Vysor or similar access the settings by swiping down on the screen in immichframe.
2. Enter your **ImmichFrame Server URL** (e.g. `http://192.168.1.x:8080`)
4. Configure display and motion sensor options (see below)
5. Tap **Close Settings**

## Motion Sensor

The motion sensor feature uses the Nixplay frame's built-in PIR hardware sensor to automatically sleep and wake the screen.

**To enable:**
1. Go to Settings
2. Turn **Keep Screen On** OFF
3. Turn **Enable Motion Sensor** ON
4. Set your **Sleep Timeout** — number of minutes of no motion before the screen sleeps (default: 15)

**How the timing works:**

Total time to sleep = motion sensor timeout + Android display sleep setting (Settings → Display → Sleep).

For example: motion sensor timeout = 5 min + display sleep = 1 min = screen goes dark 6 minutes after last motion.

Set the Android display sleep to the shortest available value (e.g. 1 minute) and control the main timeout via the in-app setting.

> The motion sensor only works on Nixplay hardware. On other Android devices the setting has no effect.

## Other Settings

| Setting | Description                                                             |
|---------|-------------------------------------------------------------------------|
| Use WebView | Load ImmichFrame via the built-in web UI (default). Disable on Nixframe |
| Keep Screen On | Keeps the screen always on. Disable when using the motion sensor        |
| Blurred Background | Show a blurred thumbhash behind portrait images (native mode only)      |
| Show Current Date | Show the current date alongside the clock (native mode only)            |
| Lock Settings | Disables swipe-to-settings. Can only be re-opened via RPC or reinstall  |
| Screen Dimming | Schedule a time range to dim the screen (e.g. `22:00-07:00`)            |

## RPC Control

The app runs a local HTTP server on port **53287** for remote control:

| Endpoint | Action |
|----------|--------|
| `GET /next` | Next image |
| `GET /previous` | Previous image |
| `GET /pause` | Pause/resume slideshow |
| `GET /dim` | Dim screen |
| `GET /undim` | Undim screen |
| `GET /brightness?value=0.5` | Set brightness (0.0–1.0, or -1 for system default) |
| `GET /settings` | Open settings screen |

## Credits

- [ImmichFrame Android](https://github.com/immichFrame/ImmichFrame_Android) — original project
- [smerschjohann](https://github.com/smerschjohann/ImmichFrame_Android_motionsensor) — motion sensor implementation