package me.glorantq.ao3

import com.google.common.base.Joiner
import com.google.gson.annotations.Expose
import org.jsoup.nodes.Document

/**
 * Object exposing information about a chapter. Contains the title, the content itself and author notes
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */
class AO3Chapter internal constructor(@Expose val workID: Int, @Expose val id: Int) : AO3Data() {
    @Expose
    val title: String

    @Expose
    val content: String

    @Expose
    val notes: List<String>

    @Expose
    val summary: String

    init {
        val document: Document = getDocument()

        title = document.selectFirst("div.chapter.preface.group").getElementsByTag("h3")[0].ownText().removePrefix(": ").trim()
        content = Joiner.on("\n").join(document.getElementsByAttributeValue("role", "article")[0].getElementsByTag("p").map { it.text() })

        notes = try {
            document.getElementsByClass("notes")
                    .filter { (it.hasAttr("role") && it.attr("role") == "complementary") || it.id() == "notes" }
                    .map { Joiner.on("\n").join(it.getElementsByClass("userstuff")[0].getElementsByTag("p").map { it.text() }) }
        } catch (e: Exception) {
            listOf()
        }

        summary = try {
            Joiner.on("\n").join(document.selectFirst("div.summary.module").selectFirst("blockquote.userstuff").getElementsByTag("p").map { it.html() })
        } catch (e: Exception) {
            ""
        }
    }

    override fun buildUrl(): String = String.format("http://archiveofourown.org/works/%d/chapters/%d", workID, id)
}