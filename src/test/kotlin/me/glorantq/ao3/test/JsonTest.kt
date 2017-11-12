package me.glorantq.ao3.test

import me.glorantq.ao3.AO3User
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
@DisplayName("Testing JSON serialising / deserialising")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonTest {

    @BeforeAll
    fun beforeAll() {
        println("=====================================")
        println("Testing JSON serialising / deserialising")
        println("=====================================")
        println("")
    }

    @Test
    @DisplayName("Testing Gson implementation")
    fun testJsonSerialising() {
        val user: AO3User = AO3User("agrestenoir")
        val jsonData: String = user.json()
        val deserialisedUser: AO3User = Utils.gson.fromJson(jsonData, AO3User::class.java)
        val secondJsonData: String = deserialisedUser.json()

        assertThat("json data", secondJsonData, equalTo(jsonData))
    }
}