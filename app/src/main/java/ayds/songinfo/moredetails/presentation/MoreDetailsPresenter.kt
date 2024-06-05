package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.MoreDetailsRepository

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MutableList<CardUIState>>

    fun updateCard(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: MoreDetailsRepository,
    private val cardDescriptionHelper: CardDescriptionHelper
) : MoreDetailsPresenter {
    override val uiStateObservable = Subject<MutableList<CardUIState>>()

    override fun updateCard(artistName: String) {
        val cards = repository.getCardsByArtistName(artistName)
        presentCards(cards)
    }

    private fun presentCards(cards: List<InfoCard>){
        val uiState: MutableList<CardUIState> = mutableListOf()

        cards.forEach { card ->
            if (card is Card){
                val cardUiState = CardUIState(
                    card.artistName,
                    cardDescriptionHelper.getCardText(card),
                    card.source,
                    card.infoUrl,
                    card.sourceLogoUrl
                )
                uiState.add(cardUiState)
            }
        }

        uiStateObservable.notify(uiState)
    }
}