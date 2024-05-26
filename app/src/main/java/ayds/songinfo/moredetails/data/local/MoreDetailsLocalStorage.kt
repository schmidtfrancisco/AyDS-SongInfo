package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.InfoCard.Card

interface MoreDetailsLocalStorage {

    fun insertCard(card: Card)

    fun getCardByArtistName(artistName: String): Card?
}

internal class MoreDetailsLocalStorageImpl(
    database: MoreDetailsDatabase
): MoreDetailsLocalStorage {

    private val cardDao: CardDao = database.cardDao()

    override fun insertCard(card: Card) {
        cardDao.insertCard(card.toCardEntity())
    }
    override fun getCardByArtistName(artistName: String): Card? {
        return cardDao.getBiographyByArtistName(artistName)?.toCard()
    }

    private fun Card.toCardEntity() = CardEntity(
        this.artistName,
        this.description,
        this.infoUrl,
        this.source
    )

    private fun CardEntity.toCard() = Card(
        this.artistName,
        this.description,
        this.infoUrl,
        this.source
    )
}
