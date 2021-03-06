package nl.ngti.androscope.server

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import fi.iki.elonen.NanoHTTPD
import nl.ngti.androscope.BuildConfig
import nl.ngti.androscope.utils.AndroscopeMetadata

/**
 * Custom HTTP server. Displays the structure of the database.
 */
internal class AndroscopeHttpServer(
        private val context: Context,
        httpPort: Int,
        metadata: AndroscopeMetadata
) : NanoHTTPD(httpPort) {

    private val responseFactory = ResponseFactory(context, metadata)

    override fun serve(session: IHTTPSession): Response? {
        return responseFactory.getResponse(session)?.apply {
            if (BuildConfig.DEBUG) {
                // Allows development with Angular running on desktop
                addHeader("Access-Control-Allow-Origin", "*")
            }
        }
    }

    val ipAddress: String
        get() {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val myIp = wifiInfo.ipAddress
            // We don't use IPv6 addresses
            @Suppress("DEPRECATION")
            return Formatter.formatIpAddress(myIp)
        }
}
