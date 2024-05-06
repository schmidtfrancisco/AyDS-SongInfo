package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.LastFMExternalService
import ayds.songinfo.moredetails.data.local.LastFMLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Biography
import ayds.songinfo.moredetails.domain.Biography.EmptyBiography
import ayds.songinfo.moredetails.domain.Biography.ArtistBiography

internal class ArtistBiographyRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMExternalService: LastFMExternalService
): ArtistBiographyRepository {

    override fun getBiographyByArtistName(artistName: String): Biography {
        var artistBiography = lastFMLocalStorage.getBiographyByArtistName(artistName)

        if (artistBiography != null) {
            markArtistBiographyAsLocal(artistBiography)
        }
        else {
            try {
                artistBiography = lastFMExternalService.getArtistBiography(artistName)

                artistBiography?.let {
                    if (it.content != LastFMExternalService.NO_CONTENT) {
                        lastFMLocalStorage.insertArtistBiography(it)
                    }
                }
            }catch (e: Exception){
                artistBiography = null
            }
        }
        return artistBiography ?: EmptyBiography
    }

    private fun markArtistBiographyAsLocal(artistBiography: ArtistBiography){
        artistBiography.isLocallyStored = true
    }
}

