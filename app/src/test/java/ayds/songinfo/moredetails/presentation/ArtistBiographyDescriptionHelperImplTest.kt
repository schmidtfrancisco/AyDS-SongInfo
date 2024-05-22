package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Biography
import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import org.junit.Test
import org.junit.Assert.assertEquals

class ArtistBiographyDescriptionHelperImplTest{

    private val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

    @Test
    fun `given a local artist biography it should return adequate HTML`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content",
            "url",
            true
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">[*]content</font></div></html>",
            result
        )
    }

    @Test
    fun `given non-local artist biography it should return adequate HTML`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content</font></div></html>",
            result
        )
    }

    @Test
    fun `given artist biography should remove apostrophes`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content's",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">contents</font></div></html>",
            result
        )
    }

    @Test
    fun `given artist biography should replace double slash`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content\\n",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content<br></font></div></html>",
            result
        )
    }

    @Test
    fun `given artist biography should replace break lines`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content\n",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content<br></font></div></html>",
            result
        )
    }

    @Test
    fun `given artist biography should set artist name bold`(){
        val artistBiography = ArtistBiography(
            "artist",
            "content artist",
            "url",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content <b>ARTIST</b></font></div></html>",
            result
        )
    }

    @Test
    fun `given no artist biography it should return adequate HTML`(){
        val biography: Biography = Biography.EmptyBiography

        val result = artistBiographyDescriptionHelper.getBiographyText(biography)

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "<b>NO RESULTS</b> found" +
                    "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given no parameters it should return adequate HTML`(){
        val result = artistBiographyDescriptionHelper.getBiographyText()

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "<b>NO RESULTS</b> found" +
                    "</font></div></html>"

        assertEquals(expected, result)

    }

}