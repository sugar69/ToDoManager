package com.example.todomanager

import android.app.Application
import io.realm.Realm

class MyToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}