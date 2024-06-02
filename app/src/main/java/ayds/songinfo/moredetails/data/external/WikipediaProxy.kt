package ayds.songinfo.moredetails.data.external

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.data.WikipediaTrackService
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.InfoCard.Source

interface WikipediaProxy {

    fun getWikipediaCard(artistName: String): InfoCard
}

internal class WikipediaProxyImpl(
    private val wikipediaTrackService: WikipediaTrackService
): WikipediaProxy {

    override fun getWikipediaCard(artistName: String): InfoCard {
        return when (val article = wikipediaTrackService.getInfo(artistName)){
            is WikipediaArticle ->
                Card(
                    artistName,
                    article.description,
                    article.wikipediaURL,
                    Source.Wikipedia,
                    article.wikipediaLogoURL
                )

            else -> EmptyCard
        }
    }
}