package me.glorantq.ao3

import com.google.gson.annotations.Expose
import org.jsoup.nodes.Document

/**
 * Object representing an AO3 user. Exposes information such as the profile picture, works and fandoms
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */
class AO3User internal constructor(@Expose val username: String, @Expose val pseud: String) : AO3Data() {
    @Expose
    val imageUrl: String

    @Expose
    val fandoms: List<String>

    @Expose
    val recentWorks: List<Int>

    init {
        val document: Document = getDocument()

        imageUrl = document.selectFirst("img.icon").attr("src")
        fandoms = try {
            document.getElementById("user-fandoms").selectFirst("ol.index.group").getElementsByTag("li").map { it.getElementsByTag("a")[0].html() }
        } catch (e: Exception) {
            listOf()
        }
        recentWorks = try {
            document.getElementById("user-works").getElementsByClass("header").map { it.getElementsByTag("h4")[0].getElementsByTag("a")[0].attr("href").removePrefix("/works/").toIntOrNull() ?: -1 }
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun buildUrl(): String = String.format("http://archiveofourown.org/users/%s/pseuds/%s", username, pseud)
}