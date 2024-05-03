package ayds.songinfo.moredetails.fulllogic.dependencyinjector

import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsActivity

object MoreDetailsViewInjector {
    fun init(moreDetailsView: MoreDetailsActivity) {
        MoreDetailsDataInjector.initMoreDetailsData(moreDetailsView)
        MoreDetailsPresenterInjector.initMoreDetailsPresenter(moreDetailsView.intent)
    }
}