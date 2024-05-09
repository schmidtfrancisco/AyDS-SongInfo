package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import retrofit2.Response

interface LastFMExternalService {

    fun getArtistBiography(artistName: String): ArtistBiography?

    companion object {
        const val NO_CONTENT = "No Results"
    }
}

internal class LastFMExternalServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToBiographyResolver: LastFMToBiographyResolver
): LastFMExternalService {

    override fun getArtistBiography(artistName: String): ArtistBiography? {
        val callResponse = getArtistBiographyFromService(artistName)
        return lastFMToBiographyResolver.getArtistBioFromExternalData(callResponse.body(), artistName)
    }

    private fun getArtistBiographyFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()
}


