package ayds.songinfo.moredetails.dependencyinjector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.ArtistBiographyRepositoryImpl
import ayds.songinfo.moredetails.data.external.JsonToBiographyResolver
import ayds.songinfo.moredetails.data.external.LastFMAPI
import ayds.songinfo.moredetails.data.external.LastFMExternalService
import ayds.songinfo.moredetails.data.external.LastFMExternalServiceImpl
import ayds.songinfo.moredetails.data.external.LastFMToBiographyResolver
import ayds.songinfo.moredetails.data.local.ArtistBiographyDatabase
import ayds.songinfo.moredetails.data.local.LastFMLocalStorage
import ayds.songinfo.moredetails.data.local.LastFMLocalStorageImpl
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.presentation.MoreDetailsActivity
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BD_NAME = "database-artist-biography"


object ArtistBiographyRepositoryInjector {
    private lateinit var artistBiographyRepository: ArtistBiographyRepository

    fun getArtistBiographyRepository(): ArtistBiographyRepository = artistBiographyRepository

    fun initArtistBiographyRepository(moreDetailsView: MoreDetailsActivity){
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