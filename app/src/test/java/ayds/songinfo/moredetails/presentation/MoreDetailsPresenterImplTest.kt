package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.InfoCard.Source
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test


class MoreDetailsPresenterImplTest {

    private val repository: MoreDetailsRepository = mockk()
    private val descriptionHelper: CardDescriptionHelper = mockk()

    private val moreDetailsPresenter: MoreDetailsPresenter =
        MoreDetailsPresenterImpl(repository, descriptionHelper)

    @Test
    fun `on getting some EmptyCard it should notify the parsed result through uiState list without this card`() {
        val card1 = Card("artistName", "content", "url", Source.LastFM, isLocallyStored = false)
        val card2 = Card("artistName", "content", "url", Source.Wikipedia, isLocallyStored = false)
        val emptyCard = EmptyCard
        val cards: List<InfoCard> = listOf(card1,card2,emptyCard)

        every { repository.getCardsByArtistName("artistName") } returns cards
        every { descriptionHelper.getCardText(card1) } returns "<html>content</html>"
        every { descriptionHelper.getCardText(card2) } returns "<html>content</html>"

        val uiStateTester: (MutableList<CardUIState>) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe(uiStateTester)

        moreDetailsPresenter.updateCard("artistName")
        val result = mutableListOf(
            CardUIState("artistName", "<html>content</html>", Source.LastFM, "url"),
            CardUIState("artistName", "<html>content</html>", Source.Wikipedia, "url")
        )
        verify { uiStateTester(result) }
    }

    @Test
    fun `on getting cards it should notify the parsed result through uiState list`() {
        val card1 = Card("artistName", "content", "url", Source.LastFM, isLocallyStored = false)
        val card2 = Card("artistName", "content", "url", Source.Wikipedia, isLocallyStored = false)
        val card3 = Card("artistName", "content", "url", Source.NewYorkTimes, isLocallyStored = false)
        val cards: List<InfoCard> = listOf(card1, card2, card3)

        every { repository.getCardsByArtistName("artistName") } returns cards
        cards.forEach { every { descriptionHelper.getCardText(it) } returns "<html>content</html>" }


        val uiStateTester: (MutableList<CardUIState>) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe(uiStateTester)

        moreDetailsPresenter.updateCard("artistName")
        val result = mutableListOf(
            CardUIState("artistName", "<html>content</html>", Source.LastFM, "url"),
            CardUIState("artistName", "<html>content</html>", Source.Wikipedia, "url"),
            CardUIState("artistName", "<html>content</html>", Source.NewYorkTimes, "url")
        )

        verify { uiStateTester(result) }
    }

}