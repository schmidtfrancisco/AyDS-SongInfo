package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.InfoCard

interface CardProxy {

    fun getCard(artistName: String): InfoCard
}