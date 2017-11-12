package me.glorantq.ao3.test

import me.glorantq.ao3.AO3Work
import me.glorantq.ao3.utils.Utils
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

/**
 * @suppress
 */
@DisplayName("Testing utilities")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UtilsTest {

    @BeforeAll
    fun beforeAll() {
        println("=====================================")
        println("Testing utilities")
        println("=====================================")
        println("")
    }

    @Test
    @DisplayName("Testing AO3 URL Encoding")
    fun testAO3URLEncode() {
        assertThat("rating", tagUrl(Utils.ao3Urlencode(AO3Work.Ratings.GENERAL.value)), equalTo("http://archiveofourown.org/tags/General%20Audiences/works"))
        assertThat("warning", tagUrl(Utils.ao3Urlencode(AO3Work.Warnings.NONE_APPLY.value)), equalTo("http://archiveofourown.org/tags/No%20Archive%20Warnings%20Apply/works"))
    }

    private fun tagUrl(value: String): String = "http://archiveofourown.org/tags/$value/works"
}