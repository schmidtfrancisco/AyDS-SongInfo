package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMBiography.LastFMArtistBiography
import ayds.artist.external.lastfm.data.LastFMExternalService
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.MoreDetailsRepository


internal class ArtistBiographyRepositoryImpl(
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage,
    private val lastFMExternalService: LastFMExternalService
): MoreDetailsRepository {

    override fun getCardByArtistName(artistName: String): InfoCard {
        var card = moreDetailsLocalStorage.getCardByArtistName(artistName)

        if (card != null) {
            markCardAsLocal(card)
        }
        else {
            val artistBiography = lastFMExternalService.getArtistBiography(artistName)

            if (artistBiography is LastFMArtistBiography) {
                card = artistBiography.toCard()

                if (card.description != LastFMExternalService.NO_CONTENT) {
                    moreDetailsLocalStorage.insertCard(card)
                }

            }
        }
        return card ?: EmptyCard
    }

    private fun markCardAsLocal(card: Card){
        card.isLocallyStored = true
    }

    private fun LastFMArtistBiography.toCard(): Card =
        Card(
            this.artistName,
            this.content,
            this.articleUrl,
            InfoCard.Source.LastFM,
            this.logoUrl
        )

}

