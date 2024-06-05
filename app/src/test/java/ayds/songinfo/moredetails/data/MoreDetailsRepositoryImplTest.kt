package ayds.songinfo.moredetails.data


import ayds.songinfo.moredetails.data.external.MoreDetailsBroker
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.Source
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class MoreDetailsRepositoryImplTest {
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage = mockk(relaxUnitFun = true)
    private val moreDetailsBroker: MoreDetailsBroker = mockk(relaxUnitFun = true)

    private val artistBiographyRepository: MoreDetailsRepository = MoreDetailsRepositoryImpl(
        moreDetailsLocalStorage,
        moreDetailsBroker
    )

    @Test
    fun `given existing cards should return them and mark them as local`(){
        val card1 = Card("artistName", "content", "url", Source.LastFM, isLocallyStored = false)
        val card2 = Card("artistName", "content", "url", Source.Wikipedia, isLocallyStored = false)
        val cards: List<Card> = listOf(card1, card2)
        every { moreDetailsLocalStorage.getCardsByArtistName("artistName") } returns cards

        val result = artistBiographyRepository.getCardsByArtistName("artistName")

        assertEquals(cards, result)
        cards.forEach { assertTrue(it.isLocallyStored) }
    }

    @Test
    fun `given non-existing local cards should get them external and store which are not empty `(){
        val card1 = Card("artistName", "content", "url", Source.LastFM, isLocallyStored = false)
        val card2 = Card("artistName", "content", "url", Source.Wikipedia, isLocallyStored = false)
        val card3 = InfoCard.EmptyCard
        val cards: List<InfoCard> = listOf(card1, card2, card3)

        every { moreDetailsLocalStorage.getCardsByArtistName("artistName") } returns listOf()
        every { moreDetailsBroker.getArtistCards("artistName") } returns cards

        val result = artistBiographyRepository.getCardsByArtistName("artistName")

        assertEquals(cards, result)
        assertFalse(card1.isLocallyStored)
        assertFalse(card2.isLocallyStored)
        verify { moreDetailsLocalStorage.insertCard(card1) }
        verify { moreDetailsLocalStorage.insertCard(card2) }

    }
}