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

    fun manageEvent(event: MoreDetailsUIEvent)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistBiographyRepository,
    private val intent: Intent
) : MoreDetailsPresenter {

    override var uiState: MoreDetailsUIState = MoreDetailsUIState()
    override val uiStateObservable = Subject<MoreDetailsUIState>()

    override fun manageEvent(event: MoreDetailsUIEvent) {
        when (event){
            MoreDetailsUIEvent.OpenWindow -> getArtistInfo()
        }
    }
    
    private fun getArtistInfo() {
        Thread {
            val artistName = getArtistName()
            val biography = repository.getBiographyByArtistName(artistName)
            presentBiography(biography)
        }.start()
    }

    private fun getArtistName() = intent.getStringExtra(MoreDetailsActivity.ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun presentBiography(biography: Biography){
        when(biography) {
            is ArtistBiography -> presentArtistBiography(biography)
            EmptyBiography -> presentEmptyBiography()
        }
    }

    private fun presentArtistBiography(artistBiography: ArtistBiography){
        uiState = uiState.copy(
            biographyText = parseText(artistBiography),
            articleUrl = artistBiography.articleUrl,
        )
        uiStateObservable.notify(uiState)
    }

    private fun presentEmptyBiography(){
        uiState = uiState.copy(
            biographyText = textToHtml("No results found", null),
            articleUrl = "",
        )
        uiStateObservable.notify(uiState)
    }

    private fun parseText(artistBiography: ArtistBiography): String{
        val text = (if (artistBiography.isLocallyStored) "[*]" else "") + artistBiography.content
        val parsedText = text.replace("\\n", "\n")
        return textToHtml(parsedText, artistBiography.artistName)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}