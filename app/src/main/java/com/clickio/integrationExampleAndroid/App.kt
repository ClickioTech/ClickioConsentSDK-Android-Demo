package com.clickio.integrationExampleAndroid

import android.app.Application
import com.clickio.clickioconsentsdk.ClickioConsentSDK
import com.clickio.clickioconsentsdk.LogsMode

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        with(ClickioConsentSDK.getInstance()) {
            setLogsMode(LogsMode.VERBOSE)
            initialize(
                context = this@App,
                config = ClickioConsentSDK.Config("241131", "en")
            )
            onReady {
                ClickioConsentSDK.getInstance().openDialog(this@App)
            }
        }
    }
}