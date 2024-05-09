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
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BD_NAME = "database-artist-biography"

object MoreDetailsInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun init(context: Context){
        val artistBiographyDatabase = Room.databaseBuilder(
            context,
            ArtistBiographyDatabase::class.java,
            ARTICLE_BD_NAME
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        val jsonToBiographyResolver: LastFMToBiographyResolver = JsonToBiographyResolver()

        val lastFMLocalStorage: LastFMLocalStorage = LastFMLocalStorageImpl(artistBiographyDatabase)
        val lastFMExternalService: LastFMExternalService = LastFMExternalServiceImpl(lastFMAPI, jsonToBiographyResolver)

        val artistBiographyRepository = ArtistBiographyRepositoryImpl(lastFMLocalStorage, lastFMExternalService)
        val artistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()

        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository, artistBiographyDescriptionHelper)
    }
}