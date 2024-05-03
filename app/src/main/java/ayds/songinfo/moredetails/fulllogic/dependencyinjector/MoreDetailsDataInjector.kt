package ayds.songinfo.moredetails.fulllogic.dependencyinjector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.ArtistBiographyRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.data.external.JsonToBiographyResolver
import ayds.songinfo.moredetails.fulllogic.data.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.data.external.LastFMExternalService
import ayds.songinfo.moredetails.fulllogic.data.external.LastFMExternalServiceImpl
import ayds.songinfo.moredetails.fulllogic.data.external.LastFMToBiographyResolver
import ayds.songinfo.moredetails.fulllogic.data.local.ArtistBiographyDatabase
import ayds.songinfo.moredetails.fulllogic.data.local.LastFMLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.local.LastFMLocalStorageImpl
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsActivity
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BD_NAME = "database-artist-biography"


object MoreDetailsDataInjector {
    private lateinit var artistBiographyRepository: ArtistBiographyRepository

    fun getArtistBiographyRepository(): ArtistBiographyRepository = artistBiographyRepository

    fun initMoreDetailsData(moreDetailsView: MoreDetailsActivity){
        val artistBiographyDatabase = Room.databaseBuilder(
            moreDetailsView as Context,
            ArtistBiographyDatabase::class.java,
            ARTICLE_BD_NAME
        ).build()

        val lastFMLocalStorage: LastFMLocalStorage = LastFMLocalStorageImpl(artistBiographyDatabase)

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        val jsonToBiographyResolver: LastFMToBiographyResolver = JsonToBiographyResolver()
        val lastFMExternalService: LastFMExternalService = LastFMExternalServiceImpl(lastFMAPI, jsonToBiographyResolver)

        artistBiographyRepository = ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMExternalService)
    }
}