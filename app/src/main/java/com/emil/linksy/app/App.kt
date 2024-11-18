package com.emil.linksy.app

import android.app.Application
import com.emil.linksy.di.appModule
import com.emil.linksy.di.dataModule
import com.emil.linksy.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application () {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, appModule))
        }
    }
}