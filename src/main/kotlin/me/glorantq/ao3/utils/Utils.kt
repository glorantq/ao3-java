package me.glorantq.ao3.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.net.URLEncoder
import java.util.*

/**
 * Various utility methods
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */
object Utils {
    private val httpClient: OkHttpClient = OkHttpClient.Builder().followRedirects(true).followSslRedirects(true).cookieJar(object : CookieJar {
        private val cookieStore = HashMap<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore.put(url.host(), cookies)
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host()]
            return cookies ?: ArrayList()
        }
    }).build()

    val gson: Gson = fun (): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        gsonBuilder.setPrettyPrinting()

        return gsonBuilder.create()
    }.invoke()

    fun syncRequest(url: String): Response {
        val request: Request = Request.Builder().url(url).build()

        return httpClient.newCall(request).execute()
    }

    fun ao3Urlencode(url: String): String =
            URLEncoder.encode(url, "UTF-8")
                    .replace("+", "%20")
                    .replace("%2f", "*s*")
}