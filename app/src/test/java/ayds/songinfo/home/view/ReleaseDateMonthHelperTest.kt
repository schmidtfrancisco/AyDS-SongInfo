package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test


class ReleaseDateMonthHelperTest {
    private val song: Song.SpotifySong = mockk()
    private val releaseDateMonthResolver: ReleaseDateMonthHelper = ReleaseDateMonthHelper(song)

    @Test
    fun `on get release date should return month 1`() {
        every { song.releaseDate } returns "1992-01"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("January, 1992", result)
    }

    @Test
    fun `on get release date should return month 2`() {
        every { song.releaseDate } returns "1992-02"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("February, 1992", result)
    }

    @Test
    fun `on get release date should return month 3`() {
        every { song.releaseDate } returns "1992-03"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("March, 1992", result)
    }

    @Test
    fun `on get release date should return month 4`() {
        every { song.releaseDate } returns "1992-04"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("April, 1992", result)
    }

    @Test
    fun `on get release date should return month 5`() {
        every { song.releaseDate } returns "1992-05"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("May, 1992", result)
    }

    @Test
    fun `on get release date should return month 6`() {
        every { song.releaseDate } returns "1992-06"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("June, 1992", result)
    }

    @Test
    fun `on get release date should return month 7`() {
        every { song.releaseDate } returns "1992-07"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("July, 1992", result)
    }

    @Test
    fun `on get release date should return month 8`() {
        every { song.releaseDate } returns "1992-08"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("August, 1992", result)
    }

    @Test
    fun `on get release date should return month 9`() {
        every { song.releaseDate } returns "1992-09"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("September, 1992", result)
    }

    @Test
    fun `on get release date should return month 10`() {
        every { song.releaseDate } returns "1992-10"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("October, 1992", result)
    }

    @Test
    fun `on get release date should return month 11`() {
        every { song.releaseDate } returns "1992-11"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("November, 1992", result)
    }

    @Test
    fun `on get release date should return month 12`() {
        every { song.releaseDate } returns "1992-12"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals("December, 1992", result)
    }

    @Test
    fun `on get release date should return month 0`() {
        every { song.releaseDate } returns "1992-00"

        val result = releaseDateMonthResolver.getSongReleaseDate()

        assertEquals(", 1992", result)
    }
}
