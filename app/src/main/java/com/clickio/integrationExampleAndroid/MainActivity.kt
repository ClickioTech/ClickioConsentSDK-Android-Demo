package com.clickio.integrationExampleAndroid

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.clickio.clickioconsentsdk.ClickioConsentSDK
import com.clickio.clickioconsentsdk.ExportData
import com.clickio.integrationExampleAndroid.ui.theme.ClickioSDK_Integration_Example_AndroidTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }
        setContent {
            ClickioSDK_Integration_Example_AndroidTheme {
                val context = LocalContext.current
                val consentData = remember { mutableStateOf<Map<String, String?>>(emptyMap()) }
                val isDataLoaded = remember { mutableStateOf(false) }

                ClickioConsentSDK.getInstance().onConsentUpdated {
                    consentData.value = loadConsentData(context)
                    isDataLoaded.value = true
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConsentScreen(
                        consentData = consentData.value,
                        modifier = Modifier.padding(innerPadding),
                        onRefresh = {
                            consentData.value = loadConsentData(context)
                            isDataLoaded.value = true
                        },
                        isDataLoaded = isDataLoaded.value
                    )
                }
            }
        }
    }
}

@Composable
fun ConsentScreen(
    consentData: Map<String, String?>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    isDataLoaded: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AdBanner(modifier = Modifier.align(Alignment.CenterHorizontally))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ConsentButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(16.dp))

        GetConsentDataButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onLoadConsentData = onRefresh,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isDataLoaded) {
            LazyColumn {
                items(consentData.entries.toList()) { (title, value) ->
                    ConsentItem(title, value.toString())
                }
            }
        }
    }
}

@Composable
fun ConsentButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(
        onClick = {
            openConsentForm(context)
        },
        modifier = modifier,
    ) {
        Text("Open Consent Window")
    }
}

@Composable
fun GetConsentDataButton(
    modifier: Modifier = Modifier,
    onLoadConsentData: () -> Unit
) {
    Button(
        onClick = { onLoadConsentData() },
        modifier = modifier,
    ) {
        Text("Get Consent Data")
    }
}

@Composable
fun ConsentItem(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Text(text = value)
        HorizontalDivider()
    }
}

private fun openConsentForm(context: Context) {
    ClickioConsentSDK.getInstance().openDialog(
        context = context,
        mode = ClickioConsentSDK.DialogMode.RESURFACE
    )
}

private fun loadConsentData(context: Context): Map<String, String?> {
    val consentSDK = ClickioConsentSDK.getInstance()
    val exportData = ExportData(context)

    val consentScope = consentSDK.checkConsentScope().toString()
    val consentState = consentSDK.checkConsentState().toString()
    val consentForPurpose = consentSDK.checkConsentForPurpose(1).toString()
    val consentForVendor = consentSDK.checkConsentForVendor(9).toString()

    val tcString = exportData.getTCString()
    val acString = exportData.getACString()
    val gppString = exportData.getGPPString().toString()
    val consentedTCFVendors = exportData.getConsentedTCFVendors().toString()
    val consentedTCFLiVendors = exportData.getConsentedTCFLiVendors().toString()
    val consentedTCFPurposes = exportData.getConsentedTCFPurposes().toString()
    val consentedTCFLiPurposes = exportData.getConsentedTCFLiPurposes().toString()
    val consentedGoogleVendors = exportData.getConsentedGoogleVendors().toString()
    val consentedOtherVendors = exportData.getConsentedOtherVendors().toString()
    val consentedOtherLiVendors = exportData.getConsentedOtherLiVendors().toString()
    val consentedNonTcfPurposes = exportData.getConsentedNonTcfPurposes().toString()
    val googleConsentMode = exportData.getGoogleConsentMode().toString()

    return mapOf(
        "checkConsentScope" to consentScope,
        "checkConsentState" to consentState,
        "checkConsentForPurpose(1)" to consentForPurpose,
        "checkConsentForVendor(9)" to consentForVendor,
        " " to " ",
        "getTCString" to tcString,
        "getACString" to acString,
        "getGPPString" to gppString,
        "getGoogleConsentMode" to googleConsentMode,
        "getConsentedTCFVendors" to consentedTCFVendors,
        "getConsentedTCFLiVendors" to consentedTCFLiVendors,
        "getConsentedTCFPurposes" to consentedTCFPurposes,
        "getConsentedTCFLiPurposes" to consentedTCFLiPurposes,
        "getConsentedGoogleVendors" to consentedGoogleVendors,
        "getConsentedOtherVendors" to consentedOtherVendors,
        "getConsentedOtherLiVendors" to consentedOtherLiVendors,
        "getConsentedNonTcfPurposes" to consentedNonTcfPurposes,
    )
}

@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test ad unit ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

