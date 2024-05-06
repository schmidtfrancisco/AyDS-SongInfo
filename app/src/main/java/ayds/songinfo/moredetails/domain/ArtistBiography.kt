package ayds.songinfo.moredetails.domain

sealed class Biography{

    data class ArtistBiography(
        val artistName: String,
        var content: String,
        var articleUrl: String,
        var isLocallyStored: Boolean = false
    ): Biography()

    object EmptyBiography: Biography()
}