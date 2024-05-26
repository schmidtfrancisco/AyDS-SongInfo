package ayds.songinfo.moredetails.domain

interface MoreDetailsRepository {

    fun getCardByArtistName(artistName: String): InfoCard

}