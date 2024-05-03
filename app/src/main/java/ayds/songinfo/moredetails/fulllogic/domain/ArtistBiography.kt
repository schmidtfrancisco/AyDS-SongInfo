package ayds.songinfo.moredetails.fulllogic.domain

sealed class Biography{

    data class ArtistBiography(
        val artistName: String,
        val content: String,
        val articleUrl: String,
    ): Biography()

    object EmptyBiography: Biography()
}