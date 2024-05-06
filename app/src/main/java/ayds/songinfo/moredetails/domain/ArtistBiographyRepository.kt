package ayds.songinfo.moredetails.domain

interface ArtistBiographyRepository {

    fun getBiographyByArtistName(artistName: String): Biography

}