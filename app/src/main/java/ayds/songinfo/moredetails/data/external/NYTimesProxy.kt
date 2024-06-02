package ayds.songinfo.moredetails.data.external

import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle.NYTimesArticleWithData
import ayds.artist.external.newyorktimes.data.NYTimesService
import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.InfoCard.Source


interface NYTimesProxy {

    fun getNYTimesCard(artistName: String): InfoCard
}

internal class NYTimesProxyImpl(
    private val nyTimesService: NYTimesService
): NYTimesProxy {

    override fun getNYTimesCard(artistName: String): InfoCard {

        return when (val article = nyTimesService.getArtistInfo(artistName)){
            is NYTimesArticleWithData ->
                Card(
                    artistName,
                    article.info?: "",
                    article.url,
                    Source.NewYorkTimes,
                    NYT_LOGO_URL
                )

            else -> EmptyCard
        }
    }
}