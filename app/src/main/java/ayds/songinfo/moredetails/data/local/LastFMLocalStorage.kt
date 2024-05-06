package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Biography.ArtistBiography

interface LastFMLocalStorage {

    fun insertArtistBiography(artistBiography: ArtistBiography)

    fun getBiographyByArtistName(artistName: String): ArtistBiography?
}