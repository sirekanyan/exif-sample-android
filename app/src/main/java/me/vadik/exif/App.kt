package me.vadik.exif

import android.app.Application
import android.os.StrictMode

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
    }
}
