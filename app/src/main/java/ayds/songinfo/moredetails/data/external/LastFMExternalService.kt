package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.Biography.ArtistBiography

interface LastFMExternalService {

    fun getArtistBiography(artistName: String): ArtistBiography?

    companion object {
        const val NO_CONTENT = "No Results"
    }
}