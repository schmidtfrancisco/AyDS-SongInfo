package ayds.artist.external.lastfm.data

import ayds.artist.external.lastfm.data.LastFMBiography.LastFMEmptyBiography
import retrofit2.Response

interface LastFMService {

    fun getArtistBiography(artistName: String): LastFMBiography

    companion object {
        const val NO_CONTENT = "No Results"
    }
}

internal class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToBiographyResolver: LastFMToBiographyResolver
): LastFMService {

    override fun getArtistBiography(artistName: String): LastFMBiography {
        return try {
            val callResponse = getArtistBiographyFromService(artistName)
            lastFMToBiographyResolver.
            getArtistBioFromExternalData(callResponse.body(), artistName) ?: LastFMEmptyBiography
        }catch (e: Exception) {
            LastFMEmptyBiography
        }

    }

    private fun getArtistBiographyFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()
}


