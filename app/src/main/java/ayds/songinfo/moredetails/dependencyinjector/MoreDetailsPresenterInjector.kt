package ayds.songinfo.moredetails.dependencyinjector

import android.content.Intent
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.presentation.MoreDetailsPresenterImpl

object MoreDetailsPresenterInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter

    fun getMoreDetailsPresenter(): MoreDetailsPresenter = moreDetailsPresenter

    fun initMoreDetailsPresenter(intent: Intent){
        val artistBiographyRepository =
            ArtistBiographyRepositoryInjector.getArtistBiographyRepository()
        moreDetailsPresenter = MoreDetailsPresenterImpl(artistBiographyRepository, intent)
    }

}