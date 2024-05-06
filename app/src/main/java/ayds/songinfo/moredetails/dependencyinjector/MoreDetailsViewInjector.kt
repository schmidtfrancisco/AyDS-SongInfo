package ayds.songinfo.moredetails.dependencyinjector

import ayds.songinfo.moredetails.presentation.MoreDetailsActivity

object MoreDetailsViewInjector {
    fun init(moreDetailsView: MoreDetailsActivity) {
        ArtistBiographyRepositoryInjector.initArtistBiographyRepository(moreDetailsView)
        MoreDetailsPresenterInjector.initMoreDetailsPresenter(moreDetailsView.intent)
    }
}