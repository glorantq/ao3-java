package me.glorantq.ao3.test

import me.glorantq.ao3.AO3User
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

/**
 * @suppress
 */
@DisplayName("Testing users")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    @BeforeAll
    fun beforeAll() {
        println("=====================================")
        println("Testing user API")
        println("=====================================")
        println("")
    }

    @Test
    @DisplayName("Testing user data")
    fun testUserData() {
        val user: AO3User = AO3User("agrestenoir", "agrestenoir")

        assertThat("username", user.username, equalTo("agrestenoir"))
        assertThat("fandoms", user.fandoms, hasSize(2))

        val secondUser: AO3User = AO3User("glorantq", "glorantq")

        assertThat("works are empty", secondUser.recentWorks, hasSize(0))
        assertThat("fandoms are empty", secondUser.fandoms, hasSize(0))

        println(user.json())
        println(secondUser.json())
    }
}