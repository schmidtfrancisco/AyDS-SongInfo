package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.MoreDetailsRepository

interface MoreDetailsPresenter {

    val uiState: MoreDetailsUIState
    val uiStateObservable: Observable<MoreDetailsUIState>

    fun getArtistInfo(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: MoreDetailsRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : MoreDetailsPresenter {

    override var uiState: MoreDetailsUIState = MoreDetailsUIState()
    override val uiStateObservable = Subject<MoreDetailsUIState>()

    override fun getArtistInfo(artistName: String) {
        val card = repository.getCardByArtistName(artistName)
        presentInfoCard(card)
    }

    private fun presentInfoCard(card: InfoCard){
        when(card) {
            is Card -> presentCard(card)
            EmptyCard -> presentEmptyCard()
        }
    }

    private fun presentCard(card: Card){
        uiState = uiState.copy(
            artistName = card.artistName,
            cardSource = card.source,
            cardDescriptionHtml = cardDescriptionHelper.getCardText(card),
            articleUrl = card.infoUrl,
            logoUrl = card.sourceLogoUrl
        )
        uiStateObservable.notify(uiState)
    }

    private fun presentEmptyCard(){
        uiState = uiState.copy(
            artistName = "",
            cardDescriptionHtml = cardDescriptionHelper.getCardText(),
            cardSource = InfoCard.Source.LastFM,
            articleUrl = "",
        )
        uiStateObservable.notify(uiState)
    }


}