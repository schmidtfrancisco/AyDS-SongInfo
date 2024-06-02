package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.InfoCard.Card

interface MoreDetailsLocalStorage {

    fun insertCard(card: Card)

    fun getCardsByArtistName(artistName: String): List<Card>
}

internal class MoreDetailsLocalStorageImpl(
    database: MoreDetailsDatabase
): MoreDetailsLocalStorage {

    private val cardDao: CardDao = database.cardDao()

    override fun insertCard(card: Card) {
        cardDao.insertCard(card.toCardEntity())
    }
    override fun getCardsByArtistName(artistName: String): List<Card> {
        val cards = cardDao.getCardByArtistName(artistName)
        return cards.toCardList()
    }

    private fun Card.toCardEntity() = CardEntity(
        this.artistName,
        this.description,
        this.infoUrl,
        this.source,
        this.sourceLogoUrl
    )

    private fun List<CardEntity>.toCardList(): List<Card> {
        return this.map { card -> Card(
            card.artistName,
            card.description,
            card.infoUrl,
            card.source,
            card.logoUrl
            )
        }
    }
}
