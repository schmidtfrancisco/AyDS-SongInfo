package ayds.songinfo.moredetails.fulllogic.data.local

import ayds.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

interface LastFMLocalStorage {

    fun insertArtistBiography(artistBiography: ArtistBiography)

    fun getBiographyByArtistName(artistName: String): ArtistBiography?
}