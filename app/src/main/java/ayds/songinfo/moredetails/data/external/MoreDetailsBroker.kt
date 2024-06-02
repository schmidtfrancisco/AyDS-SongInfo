package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.InfoCard

interface MoreDetailsBroker {

    fun getArtistCards(artistName: String): List<InfoCard>
}

internal class MoreDetailsBrokerImpl(
    private val lastFMProxy: LastFMProxy,
    private val nyTimesProxy: NYTimesProxy,
    private val wikipediaProxy: WikipediaProxy
): MoreDetailsBroker {

    override fun getArtistCards(artistName: String): List<InfoCard> {
        val lastFMCard = lastFMProxy.getLastFMCard(artistName)
        val nyTimesCard = nyTimesProxy.getNYTimesCard(artistName)
        val wikipediaCard = wikipediaProxy.getWikipediaCard(artistName)

        return listOf(lastFMCard, nyTimesCard, wikipediaCard)
    }
}