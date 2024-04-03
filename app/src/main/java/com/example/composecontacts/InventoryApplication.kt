package com.example.composecontacts

import android.app.Application
import com.example.composecontacts.data.AppContainer
import com.example.composecontacts.data.AppDataContainer

class InventoryApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
