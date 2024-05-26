package ayds.songinfo.moredetails.dependencyinjector

import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.data.LastFMExternalService
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.ArtistBiographyRepositoryImpl
import ayds.songinfo.moredetails.data.local.MoreDetailsDatabase
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorageImpl
import ayds.songinfo.moredetails.presentation.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl


private const val ARTICLE_BD_NAME = "database-card"

object MoreDetailsInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun init(context: Context){
        val moreDetailsDatabase = Room.databaseBuilder(
            context,
            MoreDetailsDatabase::class.java,
            ARTICLE_BD_NAME
        ).build()

        val moreDetailsLocalStorage: MoreDetailsLocalStorage = MoreDetailsLocalStorageImpl(moreDetailsDatabase)
        val lastFMExternalService: LastFMExternalService = LastFMInjector.lastFMExternalService

        val artistBiographyRepository = ArtistBiographyRepositoryImpl(moreDetailsLocalStorage, lastFMExternalService)
        val artistBiographyDescriptionHelper = CardDescriptionHelperImpl()

        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository, artistBiographyDescriptionHelper)
    }
}