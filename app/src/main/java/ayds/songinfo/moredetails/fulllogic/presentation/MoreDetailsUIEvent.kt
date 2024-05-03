package ayds.songinfo.moredetails.fulllogic.presentation

sealed class MoreDetailsUIEvent {

    object OpenWindow: MoreDetailsUIEvent()

    object OpenArticleUrl: MoreDetailsUIEvent()
}