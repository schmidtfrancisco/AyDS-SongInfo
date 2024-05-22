package ayds.songinfo.moredetails.presentation

import android.content.Intent
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Biography
import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import ayds.songinfo.moredetails.domain.Biography.EmptyBiography
import java.util.Locale

interface MoreDetailsPresenter {

    val uiState: MoreDetailsUIState
    val uiStateObservable: Observable<MoreDetailsUIState>

    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistBiographyRepository,
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper
) : MoreDetailsPresenter {

    override var uiState: MoreDetailsUIState = MoreDetailsUIState()
    override val uiStateObservable = Subject<MoreDetailsUIState>()

    override fun getArtistInfo(artistName: String) {
        val biography = repository.getBiographyByArtistName(artistName)
        presentBiography(biography)
    }

    private fun presentBiography(biography: Biography){
        when(biography) {
            is ArtistBiography -> presentArtistBiography(biography)
            EmptyBiography -> presentEmptyBiography()
        }
    }

    private fun presentArtistBiography(artistBiography: ArtistBiography){
        uiState = uiState.copy(
            artistName = artistBiography.artistName,
            biographyTextHtml = artistBiographyDescriptionHelper.getBiographyText(artistBiography),
            articleUrl = artistBiography.articleUrl,
        )
        uiStateObservable.notify(uiState)
    }

    private fun presentEmptyBiography(){
        uiState = uiState.copy(
            artistName = "",
            biographyTextHtml = artistBiographyDescriptionHelper.getBiographyText(),
            articleUrl = "",
        )
        uiStateObservable.notify(uiState)
    }


}