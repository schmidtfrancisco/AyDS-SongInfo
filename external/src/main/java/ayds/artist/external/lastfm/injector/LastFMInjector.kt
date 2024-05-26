package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.JsonToBiographyResolver
import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastFMExternalService
import ayds.artist.external.lastfm.data.LastFMExternalServiceImpl
import ayds.artist.external.lastfm.data.LastFMToBiographyResolver
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"

object LastFMInjector {
    private val jsonToBiographyResolver: LastFMToBiographyResolver = JsonToBiographyResolver()
    private val lastFMAPI = getLastFMAPI()

    val lastFMExternalService: LastFMExternalService = LastFMExternalServiceImpl(lastFMAPI, jsonToBiographyResolver)

    private fun getLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(LastFMAPI::class.java)
    }

}