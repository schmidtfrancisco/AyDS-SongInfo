package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Biography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.assertEquals


class MoreDetailsPresenterImplTest {

    private val repository: ArtistBiographyRepository = mockk()
    private val descriptionHelper: ArtistBiographyDescriptionHelper = mockk()

    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(repository, descriptionHelper)

    @Test
    fun `on getting EmptyBiography it should notify the parsed result through uiState with no articleUrl`(){
        val biography = Biography.EmptyBiography
        every { repository.getBiographyByArtistName("artistName") } returns biography
        every { descriptionHelper.getBiographyText(biography) } returns "<html></html>"

        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe{
            uiStateTester(it)
            assertEquals("", it.articleUrl)
        }

        moreDetailsPresenter.getArtistInfo("artistName")

        verify { uiStateTester(moreDetailsPresenter.uiState) }
    }

    @Test
    fun `on getting ArtistBiography it should notify the parsed result through uiState`(){
        val biography = Biography.ArtistBiography(
            "artistName",
            "content",
            "url"
        )
        every { repository.getBiographyByArtistName("artistName") } returns biography
        every { descriptionHelper.getBiographyText(biography) } returns "<html>content</html>"

        val uiStateTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe{
            uiStateTester(it)
        }

        moreDetailsPresenter.getArtistInfo("artistName")

        verify { uiStateTester(moreDetailsPresenter.uiState) }
    }
}