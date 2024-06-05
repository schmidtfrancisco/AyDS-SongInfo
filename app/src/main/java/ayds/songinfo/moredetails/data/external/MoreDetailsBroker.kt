package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.InfoCard

interface MoreDetailsBroker {

    fun getArtistCards(artistName: String): List<InfoCard>
}

internal class MoreDetailsBrokerImpl(
    private val proxies: List<CardProxy>
): MoreDetailsBroker {

    override fun getArtistCards(artistName: String): List<InfoCard> {
        return proxies.map { it.getCard(artistName) }
    }
}