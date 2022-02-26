package promax.dohaumen.financeapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}

/**
 Currency: Bank, Cash
 Money Type: Type in, Type out
 Type: sales money, invest
 **/
