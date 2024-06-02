package ayds.songinfo.moredetails.domain

interface MoreDetailsRepository {

    fun getCardsByArtistName(artistName: String): List<InfoCard>

}