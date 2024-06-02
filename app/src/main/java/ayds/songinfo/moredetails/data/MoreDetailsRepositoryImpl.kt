package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.MoreDetailsBroker
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.MoreDetailsRepository


internal class MoreDetailsRepositoryImpl(
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage,
    private val moreDetailsBroker: MoreDetailsBroker
): MoreDetailsRepository {

    override fun getCardsByArtistName(artistName: String): List<InfoCard> {
        val dbCards: List<Card> = moreDetailsLocalStorage.getCardsByArtistName(artistName)

        val cards: List<InfoCard>

        if (dbCards.isNotEmpty()) {
            markCardsAsLocal(dbCards)
            cards = dbCards
        }
        else {
            cards = moreDetailsBroker.getArtistCards(artistName)

            cards.forEach { infoCard ->
                if (infoCard is Card)
                    moreDetailsLocalStorage.insertCard(infoCard)
            }
        }
        return cards
    }

    private fun markCardsAsLocal(cards: List<Card>){
        cards.forEach { card -> card.isLocallyStored = true }
    }
}

