package ayds.songinfo.moredetails.fulllogic.data.external

import ayds.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

interface LastFMExternalService {

    fun getArtistBiography(artistName: String): ArtistBiography?
}