package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test


class ReleaseDateYearHelperTest {

    private val song: Song.SpotifySong = mockk()
    private val releaseDateYearHelper: ReleaseDateYearHelper = ReleaseDateYearHelper(song)

    @Test
    fun `on get release date should return not leap year`() {
        every { song.releaseDate } returns "1991"

        val result = releaseDateYearHelper.getSongReleaseDate()

        assertEquals(result, "1991 (not a leap year)")
    }
    @Test
    fun `on get release date should return leap year`() {
        every { song.releaseDate } returns "2024"

        val result = releaseDateYearHelper.getSongReleaseDate()

        assertEquals(result, "2024 (a leap year)")
    }
}