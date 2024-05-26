package ayds.songinfo.moredetails.domain

sealed class InfoCard{

    data class Card(
        val artistName: String,
        var description: String,
        var infoUrl: String,
        var source: Source,
        var sourceLogoUrl: String = "",
        var isLocallyStored: Boolean = false
    ): InfoCard()

    object EmptyCard: InfoCard()

    enum class Source {
        LastFM, Wikipedia, NewYorkTimes
    }
}