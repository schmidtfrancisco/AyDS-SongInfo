package ayds.songinfo.moredetails.data.external

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.artist.external.lastfm.data.LastFMBiography.LastFMArtistBiography
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.InfoCard.Source

internal class LastFMProxyImpl(
    private val lastFMService: LastFMService
):CardProxy {

    override fun getCard(artistName: String): InfoCard {
        return when (val biography = lastFMService.getArtistBiography(artistName)) {
            is LastFMArtistBiography ->
                Card(
                    artistName,
                    biography.content,
                    biography.articleUrl,
                    Source.LastFM,
                    LASTFM_LOGO_URL,
                )

            else -> EmptyCard
        }
    }
}