# Clickio Consent - Android Sample App (Kotlin)

## Description

This sample app demonstrates how to integrate the [Clickio Consent SDK](https://github.com/ClickioTech/ClickioConsentSDK-Android) into a basic Android application using Kotlin.

In particular, it shows how to:

- Initialize the SDK with your site configuration
- Show a consent dialog on app launch
- Open the dialog again manually in resurface mode
- Display all received consent data on button click

## Setup & Run

Installation is simple:

1. Clone or download the sample project.
2. Open it in **Android Studio**.
3. Build and run the app on a device or emulator.

No additional configuration is needed to get started — everything is included in the sample.

## Main Content

### `App.kt`

Initializes the Clickio Consent SDK in the `Application` class:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        with(ClickioConsentSDK.getInstance()) {
            setLogsMode(LogsMode.VERBOSE)
            initialize(
                context = this@App,
                config = ClickioConsentSDK.Config("241131", "en") // Replace "241131" with your own Site ID
            )
        }
    }
}
```

- The `241131` in the line `config = ClickioConsentSDK.Config("241131", "en")` can be replaced with your own site identifier provided by Clickio.


### `MainActivity.kt`

Shows Consent Dialog on SDK's readiness and ads, defines a simple UI with three buttons:

- **"Open Consent Window in Resurface mode"**  
  Opens the consent dialog in **resurface** mode, allowing the user to review or change their preferences.

- **"Reload Consent Data from SharedPreferences"**  
  Retrieves all stored consent data from `SharedPreferences` and displays it on screen. Useful for debugging and verifying SDK behavior.

- **"Clear Data"**  
  Clears default `SharedPreferences`
