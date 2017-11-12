package me.glorantq.ao3.test

import me.glorantq.ao3.AO3Chapter
import me.glorantq.ao3.AO3User
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
@DisplayName("Testing chapters")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChapterTest {

    @BeforeAll
    fun beforeAll() {
        println("=====================================")
        println("Testing chapter API")
        println("=====================================")
        println("")
    }

    @Test
    @DisplayName("Testing chapter data")
    fun testChapters() {
        val work: AO3Work = AO3Work(8480863)
        val chapter: AO3Chapter = work.getChapter(19433773)

        val secondWork: AO3Work = AO3Work(10731414)
        val secondChapter: AO3Chapter = secondWork.getChapter(23782818)

        val thirdWork: AO3Work = AO3Work(11155020)
        val thirdChapter: AO3Chapter = thirdWork.getChapter(24892074)

        val adultChapter: AO3Chapter = AO3Chapter(11674323, 26273259)

        println(chapter.json())
        println(secondChapter.json())
        println(thirdChapter.json())
        println(adultChapter.json())
    }
}