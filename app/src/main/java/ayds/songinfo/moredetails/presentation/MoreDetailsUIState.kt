package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.InfoCard

data class MoreDetailsUIState(
    val artistName: String = "",
    val cardDescriptionHtml: String = "",
    val cardSource: InfoCard.Source? = null,
    val articleUrl: String = "",
    val logoUrl: String = ""
)