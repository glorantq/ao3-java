package me.glorantq.ao3

import me.glorantq.ao3.utils.AO3Exception
import me.glorantq.ao3.utils.AO3Utils
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

/**
 * Main API class
 *
 * @author Gerber Lóránt Viktor
 * @since 1.1
 */
object AO3 {
    fun getWork(workID: Int): AO3Work = try {
        AO3Work(workID)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain work. Most likely a parsing error!", e)
    }

    fun getUser(username: String): AO3User = try {
        AO3User(username, username)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain user. Most likely a parsing error!", e)
    }

    fun getPseud(username: String, pseud: String): AO3User = try {
        AO3User(username, pseud)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain user. Most likely a parsing error!", e)
    }

    private fun searchWork0(query: String, warnings: AO3Work.Warnings?, rating: AO3Work.Ratings?): List<AO3Work> {
        val urlBuilder = StringBuilder()
        urlBuilder.append("http://archiveofourown.org/works/search?utf8=✓&work_search%5Bquery%5D=")
        urlBuilder.append(AO3Utils.ao3Urlencode(query))
        if(warnings != null) {
            urlBuilder.append(" AND \"")
            urlBuilder.append(warnings.value.toLowerCase())
            urlBuilder.append("\"")
        }

        if(rating != null) {
            urlBuilder.append(" AND \"")
            urlBuilder.append(rating.value.toLowerCase())
            urlBuilder.append("\"")
        }

        val response: Response = AO3Utils.syncRequest(urlBuilder.toString())
        if(response.code() != 200) {
            throw AO3Exception("Invalid response code: ${response.code()}")
        }

        val rawBody: ResponseBody = response.body() ?: throw AO3Exception("No body returned!")
        val document: Document = Jsoup.parse(rawBody.string())

        val resultList: Element = document.selectFirst("ol.work.index.group")
        val results: Elements = resultList.select("li.work.blurb.group")
        val workIDs: List<Int> = results.map { it.id().substring(5).toIntOrNull() ?: -1 }

        val works: MutableList<AO3Work> = mutableListOf()
        workIDs.forEach {
            try {
                works.add(getWork(it))
            } catch (e: AO3Exception) {
                e.printStackTrace()
            }
        }

        return works
    }

    fun searchWork(query: String, warnings: AO3Work.Warnings?, rating: AO3Work.Ratings?): List<AO3Work> = try {
        searchWork0(query, warnings, rating)
    } catch (e: Exception) {
        throw AO3Exception("Failed to search for works! Most likely a parsing error!", e)
    }

    fun searchWork(query: String): List<AO3Work> = searchWork(query, null, null)
    fun searchWork(query: String, warnings: AO3Work.Warnings?) = searchWork(query, warnings, null)
    fun searchWork(query: String, rating: AO3Work.Ratings?) = searchWork(query, null, rating)
}