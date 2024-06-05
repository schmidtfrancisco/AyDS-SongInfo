package ayds.songinfo.moredetails.dependencyinjector

import android.content.Context
import androidx.room.Room
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.MoreDetailsRepositoryImpl
import ayds.songinfo.moredetails.data.external.CardProxy
import ayds.songinfo.moredetails.data.external.LastFMProxyImpl
import ayds.songinfo.moredetails.data.external.MoreDetailsBrokerImpl
import ayds.songinfo.moredetails.data.external.NYTimesProxyImpl
import ayds.songinfo.moredetails.data.external.WikipediaProxyImpl
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
        val lastFMProxy: CardProxy = LastFMProxyImpl(LastFMInjector.lastFMService)
        val nyTimesProxy: CardProxy = NYTimesProxyImpl(NYTimesInjector.nyTimesService)
        val wikipediaProxy: CardProxy = WikipediaProxyImpl(WikipediaInjector.wikipediaTrackService)

        val moreDetailsBroker = MoreDetailsBrokerImpl(listOf(lastFMProxy, nyTimesProxy, wikipediaProxy))

        val moreDetailsRepository = MoreDetailsRepositoryImpl(moreDetailsLocalStorage, moreDetailsBroker)
        val cardDescriptionHelper = CardDescriptionHelperImpl()

        moreDetailsPresenter = MoreDetailsPresenterImpl(moreDetailsRepository, cardDescriptionHelper)
    }
}