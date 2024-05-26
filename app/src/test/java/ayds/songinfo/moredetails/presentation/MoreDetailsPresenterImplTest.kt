package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.domain.Card
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test


class MoreDetailsPresenterImplTest {

    private val repository: MoreDetailsRepository = mockk()
    private val descriptionHelper: CardDescriptionHelper = mockk()

    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, descriptionHelper)

    @Test
    fun `on getting EmptyBiography it should notify the parsed result through uiState with no articleUrl`(){
        val biography = Card.EmptyBiography
        every { repository.getBiographyByArtistName("artistName") } returns biography
        every { descriptionHelper.getBiographyText(biography) } returns "<html></html>"

        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe(uiStateTester)

        moreDetailsPresenter.getArtistInfo("artistName")
        val result = MoreDetailsUIState("", "<html></html>", "")

        verify { uiStateTester(result) }
    }

    @Test
    fun `on getting ArtistBiography it should notify the parsed result through uiState`(){
        val biography = Card.ArtistBiography(
            "artistName",
            "content",
            "url"
        )
        every { repository.getBiographyByArtistName("artistName") } returns biography
        every { descriptionHelper.getBiographyText(biography) } returns "<html>content</html>"

        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe(uiStateTester)

        moreDetailsPresenter.getArtistInfo("artistName")
        val result = MoreDetailsUIState("artistName", "<html></html>", "url")

        verify { uiStateTester(moreDetailsPresenter.uiState) }
    }
}