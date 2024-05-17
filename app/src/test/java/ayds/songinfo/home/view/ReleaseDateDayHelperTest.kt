package ayds.songinfo.home.view

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test


class ReleaseDateDayHelperTest {

    @Test
    fun `on get release date should return day 1`() {
        val releaseDateDayHelper =
            ReleaseDateDayHelper(mockk { every { releaseDate } returns "1992-02-01" })

        val result = releaseDateDayHelper.getSongReleaseDate()

        assertEquals("01/02/1992", result)
    }

}