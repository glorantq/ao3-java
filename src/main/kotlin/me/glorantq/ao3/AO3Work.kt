package me.glorantq.ao3

import com.google.common.base.Joiner
import com.google.gson.annotations.Expose
import me.glorantq.ao3.utils.AO3Exception
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import kotlin.collections.HashMap

/**
 * Object representing a work on AO3. Contains statistics, metadata and chapter information
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */

class AO3Work internal constructor(@Expose val id: Int) : AO3Data() {
    @Expose
    val title: String

    @Expose
    val authors: List<AO3User>

    @Expose
    val archiveWarning: Warnings

    @Expose
    val rating: Ratings

    @Expose
    val category: Categories

    @Expose
    val fandom: String

    @Expose
    val relationships: List<String>

    @Expose
    val characters: List<String>

    @Expose
    val additionalTags: List<String>

    @Expose
    val language: String

    @Expose
    val stats: Map<String, String>

    @Expose
    val published: Date

    @Expose
    val updated: Date

    @Expose
    val chapters: Map<Int, String>

    init {
        errorMappings.put(404, "Cannot find work with specified ID")

        val document: Document = getDocument()

        title = document.selectFirst("h2.title.heading").html()
        val pseudRegex: Regex = Regex("\\((.*?)\\)")
        val tempAuthors: MutableList<AO3User> = mutableListOf()
        document.selectFirst("h3.byline.heading").getElementsByTag("a").map { it.html() }.forEach {
            if(it.contains('(') && it.contains(')')) {
                val username: String = pseudRegex.find(it)!!.groupValues[1]
                val pseud: String = it.split(" ")[0].trim()
                tempAuthors.add(AO3.getPseud(username, pseud))
            } else {
                tempAuthors.add(AO3.getUser(it))
            }
        }
        authors = tempAuthors

        archiveWarning = Warnings.byValue(getArhiveTag("warning", document))
        rating = Ratings.byValue(getArhiveTag("rating", document))
        category = try { Categories.byValue(getArhiveTag("category", document)) } catch (e: Exception) { Categories.NONE }
        fandom = getWorkTag("fandom", document)
        relationships = try { document.selectFirst("dd.relationship.tags").getElementsByTag("ul")[0].getElementsByTag("li").map { it.getElementsByTag("a")[0].html() } } catch (e: Exception) { emptyList() }
        characters = try { getTagList("character", document) } catch (e: Exception) { emptyList() }
        additionalTags = try { getTagList("freeform", document) } catch (e: Exception) { emptyList() }
        language = document.selectFirst("dd.language").html()

        val tempStats: HashMap<String, String> = HashMap()
        document.selectFirst("dl.stats").getElementsByTag("dd")
                .filterNot {it.className() == "bookmarks" || it.className() == "status" || it.className() == "published"}
                .forEach { tempStats.put(it.className(), it.html()) }
        try {
            tempStats.put("bookmarks", document.selectFirst("dd.bookmarks").getElementsByTag("a")[0].html())
        } catch (e: Exception) {
            tempStats.put("bookmarks", "0")
        }
        stats = tempStats

        published = getDate("published", document)
        updated = try {
            getDate("status", document)
        } catch (e: Exception) {
             published
        }

        val tempChapters: HashMap<Int, String> = HashMap()
        try {
            document.getElementById("selected_id").getElementsByTag("option").forEach { tempChapters.put(it.attr("value").toIntOrNull() ?: -1, it.html()) }
        } catch (e: Exception) {
            tempChapters.put(id, title)
        }
        chapters = tempChapters.toSortedMap()
    }

    override fun buildUrl(): String = String.format("https://archiveofourown.org/works/%d", id)

    private fun getArhiveTag(css: String, document: Document): String =
            document.selectFirst("dd.$css.tags").getElementsByTag("ul")[0].getElementsByTag("li")[0].selectFirst("a.tag").html()

    private fun getWorkTag(css: String, document: Document): String =
            document.selectFirst("dd.$css.tags").getElementsByTag("ul")[0].getElementsByTag("li")[0].selectFirst("a.tag").html()

    private fun getTagList(css: String, document: Document): List<String> =
            document.selectFirst("dd.$css.tags").getElementsByTag("ul")[0].getElementsByTag("li").map { it.selectFirst("a.tag").html() }

    private fun getDate(css: String, document: Document): Date =
            SimpleDateFormat("yyyy-MM-dd").parse(document.selectFirst("dl.stats").getElementsByClass(css)[1].html())

    /**
     * Returns an AO3Chapter based on a chapter ID
     *
     * @see AO3Chapter
     * @author Gerber Lóránt Viktor
     * @since 1.0
     */
    fun getChapter(chapterID: Int): AO3Chapter {
        if(!chapters.containsKey(chapterID)) {
            throw AO3Exception("Chapter not found! Either the ID is invalid or doesn't exist in the current work object")
        }

        return AO3Chapter(id, chapterID)
    }

    /**
     * Enumeration of archive warnings
     *
     * @author Gerber Lóránt Viktor
     * @since 1.0
     */
    public enum class Warnings(val value: String) {
        NO_WARNINGS("Creator Chose Not To Use Archive Warnings"),
        NONE_APPLY("No Archive Warnings Apply"),
        VIOLENCE("Graphic Depictions Of Violence"),
        MAJOR_CHARACTER_DEATH("Major Character Death"),
        NON_CON("Rape/Non-Con"),
        UNDERAGE("Underage");

        companion object {
            fun byValue(value: String): Warnings = values().first { it.value.equals(value, true) }
        }
    }

    /**
     * Enumeration of archive ratings
     *
     * @author Gerber Lóránt Viktor
     * @since 1.0
     */
    public enum class Ratings(val value: String) {
        NOT_RATED("Not Rated"),
        GENERAL("General Audiences"),
        TEEN_AND_UP("Teen And Up Audiences"),
        MATURE("Mature"),
        EXPLICIT("Explicit");

        companion object {
            fun byValue(value: String): Ratings = Ratings.values().first { it.value.equals(value, true) }
        }
    }

    /**
     * Enumeration of archive categories
     *
     * @author Gerber Lóránt Viktor
     * @since 1.0
     */
    public enum class Categories(val value: String) {
        F_F("F/F"),
        F_M("F/M"),
        GEN("Gen"),
        M_M("M/M"),
        MULTI("Multi"),
        OTHER("Other"),
        NONE("None");

        companion object {
            fun byValue(value: String): Categories = Categories.values().first { it.value.equals(value, true) }
        }
    }
}