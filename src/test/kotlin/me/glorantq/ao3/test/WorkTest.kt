package me.glorantq.ao3.test

import me.glorantq.ao3.AO3
import me.glorantq.ao3.AO3Work
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.util.*

/**
 * @suppress
 */
@DisplayName("Testing works")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkTest {

    @BeforeAll
    fun beforeAll() {
        println("=====================================")
        println("Testing works API")
        println("=====================================")
        println("")
    }

    @Test
    @DisplayName("Testing work data")
    fun testWorkData() {
        val work: AO3Work = AO3Work(11907219)

        assertThat("work title", work.title, equalTo("eiffel for you"))
        assertThat("work author", work.authors, hasItem("agrestenoir"))
        assertThat("rating", work.rating, equalTo(AO3Work.Ratings.GENERAL))
        assertThat("warnings", work.archiveWarning, equalTo(AO3Work.Warnings.NONE_APPLY))
        assertThat("language", work.language, equalTo("English"))
        assertThat("characters contains", work.characters, hasItem("Chat Noir"))
        assertThat("tags contains", work.additionalTags, hasItem("Drama"))

        println(work.json())
    }

    @Test
    @DisplayName("Testing adult work data")
    fun testAdultWorkData() {
        val work: AO3Work = AO3Work(11674323)

        println(work.json())
    }

    @Test
    @DisplayName("Testing search")
    fun testSearch() {
        val works: List<AO3Work> = AO3.searchWork("Breeze")
        assertThat("generic search size", works.size, equalTo(20))
        val complexSearch: List<AO3Work> = AO3.searchWork("Breeze AND 3laxx", AO3Work.Warnings.VIOLENCE, AO3Work.Ratings.MATURE)
        assertThat("complex search size", complexSearch.size, equalTo(1))
    }

    @Test
    fun testEdenDaphne() {
        val work: AO3Work = AO3.getWork(9523928)
        println(work.authors.map { it.buildUrl() })
    }
}