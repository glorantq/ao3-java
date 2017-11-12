package me.glorantq.ao3

import me.glorantq.ao3.utils.AO3Exception
import me.glorantq.ao3.utils.AO3Utils
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Superclass for all other data classes. Contains utility methods to get a JSoup document, convert to JSON and a URL
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */
abstract class AO3Data {
    protected val errorMappings: MutableMap<Int, String> = HashMap()

    /**
     * Method used in subclasses to get the document
     *
     * @return A JSoup parse document
     */
    protected fun getDocument(): Document = getDocument(buildUrl(), 0)

    /**
     * Internal method to retrieve the page.
     *
     * @param url The URL to get
     * @param depth The number of passes (used when dealing with adult works)
     * @return  A JSoup parsed document
     */
    private fun getDocument(url: String, depth: Int): Document {
        val response: Response = AO3Utils.syncRequest(url)
        if(response.code() != 200) {
            if(errorMappings.containsKey(response.code())) {
                throw AO3Exception("Invalid status code from AO3 (${response.code()})")
            } else {
                throw AO3Exception("Invalid status code from AO3: ${errorMappings[response.code()]}")
            }
        } else {
            if(response.body() == null) {
                throw AO3Exception("No body returned")
            } else {
                val body: String = response.body()!!.string()
                if(body.contains("This work could have adult content", true)) {
                    if(depth == 9) {
                        throw AO3Exception("Too many redirects in adult work confirmation!")
                    }

                    return getDocument("$url?view_adult=true", depth + 1)
                }

                if(body.contains("This work is only available to registered users", true)) {
                    throw AO3Exception("This work is only available to registered users!")
                }

                return Jsoup.parse(body)
            }
        }
    }

    /**
     * Converts this object to JSON
     *
     * @return A JSON representation of the obejct
     */
    fun json(): String = AO3Utils.gson.toJson(this)

    companion object {
        /**
         * Converts json back into the object
         */
        fun <T> fromJson(json: String, clazz: Class<*>): T = AO3Utils.gson.fromJson(json, clazz) as T
    }

    /**
     * Builds a URL that can be queried for information
     */
    abstract fun buildUrl(): String
}