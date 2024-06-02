package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.InfoCard

data class CardUIState(
    val artistName: String = "",
    val cardDescriptionHtml: String = "",
    val cardSource: InfoCard.Source? = null,
    val url: String = "",
    val logoUrl: String = ""
)