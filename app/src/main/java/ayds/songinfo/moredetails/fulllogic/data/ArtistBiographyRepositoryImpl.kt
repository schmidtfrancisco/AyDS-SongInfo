package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.data.external.LastFMExternalService
import ayds.songinfo.moredetails.fulllogic.data.local.LastFMLocalStorage
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.fulllogic.domain.Biography
import ayds.songinfo.moredetails.fulllogic.domain.Biography.EmptyBiography
import ayds.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

private const val NO_RESULTS = "No Results"

internal class ArtistBiographyRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMExternalService: LastFMExternalService
): ArtistBiographyRepository {

    override fun getBiographyByArtistName(artistName: String): Biography {
        var artistBiography = lastFMLocalStorage.getBiographyByArtistName(artistName)
        artistBiography?.markItAsLocal()

        if (artistBiography == null){
            try {
                artistBiography = lastFMExternalService.getArtistBiography(artistName)

                artistBiography?.let {
                    if (it.content != NO_RESULTS) {
                        lastFMLocalStorage.insertArtistBiography(it)
                    }
                }
            }catch (e: Exception){
                artistBiography = null
            }
        }
        return artistBiography ?: EmptyBiography
    }

    private fun ArtistBiography.markItAsLocal() = copy(content = "[*]$content")
}

