package ayds.artist.external.lastfm.data

sealed class LastFMBiography {

    data class LastFMArtistBiography(
        val artistName: String,
        var content: String,
        var articleUrl: String,
        var logoUrl: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    ): LastFMBiography()

    object LastFMEmptyBiography: LastFMBiography()


}