package com.clickio.integrationExampleAndroid

import android.app.Application
import com.clickio.clickioconsentsdk.ClickioConsentSDK
import com.clickio.clickioconsentsdk.LogsMode

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ClickioConsentSDK.getInstance().setLogsMode(LogsMode.VERBOSE)
        ClickioConsentSDK.getInstance()
            .initialize(
                context = this,
                config = ClickioConsentSDK.Config("241131", "en")
            )
    }
}