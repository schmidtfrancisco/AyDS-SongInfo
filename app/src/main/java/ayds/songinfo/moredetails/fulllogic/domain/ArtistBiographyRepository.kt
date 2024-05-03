package ayds.songinfo.moredetails.fulllogic.domain

interface ArtistBiographyRepository {

    fun getBiographyByArtistName(artistName: String): Biography

}