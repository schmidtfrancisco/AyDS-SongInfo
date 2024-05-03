package ayds.songinfo.moredetails.fulllogic.dependencyinjector

import android.content.Intent
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresenterInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(intent: Intent){
        val artistBiographyRepository = MoreDetailsDataInjector.getArtistBiographyRepository()
        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository, intent)
    }

}