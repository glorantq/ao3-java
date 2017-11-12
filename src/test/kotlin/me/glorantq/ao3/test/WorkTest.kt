package me.glorantq.ao3.test

import me.glorantq.ao3.AO3Work
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

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
        val work: AO3Work = AO3Work(10731414)

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
}