package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.Source
import org.junit.Assert.assertEquals
import org.junit.Test

class CardDescriptionHelperImplTest{

    private val cardDescriptionHelper = CardDescriptionHelperImpl()

    @Test
    fun `given a local card it should return adequate HTML`(){
        val card = Card(
            "artist",
            "content",
            "url",
            Source.Wikipedia,
            isLocallyStored = true
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">[*]content</font></div></html>",
            result
        )
    }

    @Test
    fun `given non-local card it should return adequate HTML`(){
        val card = Card(
            "artist",
            "content",
            "url",
            Source.Wikipedia,
            isLocallyStored = false
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content</font></div></html>",
            result
        )
    }

    @Test
    fun `given card should remove apostrophes`(){
        val card = Card(
            "artist",
            "content's",
            "url",
            Source.Wikipedia,
            isLocallyStored = false
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">contents</font></div></html>",
            result
        )
    }

    @Test
    fun `given card should replace double slash`(){
        val card = Card(
            "artist",
            "content\\n",
            "url",
            Source.Wikipedia,
            isLocallyStored = false
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content<br></font></div></html>",
            result
        )
    }

    @Test
    fun `given card should replace break lines`(){
        val card = Card(
            "artist",
            "content\n",
            "url",
            Source.Wikipedia,
            isLocallyStored = false
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content<br></font></div></html>",
            result
        )
    }

    @Test
    fun `given card should set artist name bold`(){
        val card = Card(
            "artist",
            "content artist",
            "url",
            Source.Wikipedia,
            isLocallyStored = false
        )

        val result = cardDescriptionHelper.getCardText(card)

        assertEquals(
            "<html><div width=400><font face=\"arial\">content <b>ARTIST</b></font></div></html>",
            result
        )
    }

    @Test
    fun `given no card it should return adequate HTML`(){
        val card: InfoCard = InfoCard.EmptyCard

        val result = cardDescriptionHelper.getCardText(card)

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "<b>NO RESULTS</b> found" +
                    "</font></div></html>"

        assertEquals(expected, result)
    }

    @Test
    fun `given no parameters it should return adequate HTML`(){
        val result = cardDescriptionHelper.getCardText()

        val expected =
            "<html><div width=400><font face=\"arial\">" +
                    "<b>NO RESULTS</b> found" +
                    "</font></div></html>"

        assertEquals(expected, result)

    }

}