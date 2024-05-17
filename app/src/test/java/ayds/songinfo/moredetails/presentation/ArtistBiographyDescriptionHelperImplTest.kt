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
            "Marcelo Martinez",
            "Marcelo Martinez nacio en Japón era apodado 'chucho'.\\n" +
                    "Marce vivió por más de 5 años en Buenos Aires.\\n" +
                    "Martinez era un genio del rock nacional.\\n " +
                    "Marcelo falleció en 1985",
            "",
            true
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "[*]<b>MARCELO MARTINEZ</b> nacio en Japón era apodado chucho.<br>" +
                    "Marce vivió por más de 5 años en Buenos Aires.<br>" +
                    "Martinez era un genio del rock nacional.<br> " +
                    "Marcelo falleció en 1985" +
                    "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non-local artist biography it should return adequate HTML`(){
        val artistBiography = ArtistBiography(
            "Marcelo Martinez",
            "Marcelo Martinez nacio en Japón era apodado 'chucho'.\\n" +
                    "Marce vivió por más de 5 años en Buenos Aires.\\n" +
                    "Martinez era un genio del rock nacional.\\n " +
                    "Marcelo falleció en 1985",
            "",
            false
        )

        val result = artistBiographyDescriptionHelper.getBiographyText(artistBiography)

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "<b>MARCELO MARTINEZ</b> nacio en Japón era apodado chucho.<br>" +
                    "Marce vivió por más de 5 años en Buenos Aires.<br>" +
                    "Martinez era un genio del rock nacional.<br> " +
                    "Marcelo falleció en 1985" +
                    "</font></div></html>"

        assertEquals(expected, result)
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