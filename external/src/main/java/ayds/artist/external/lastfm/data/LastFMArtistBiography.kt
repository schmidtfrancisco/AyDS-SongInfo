package ayds.artist.external.lastfm.data

const val LASTFM_LOGO_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

sealed class LastFMBiography {

    data class LastFMArtistBiography(
        val artistName: String,
        var content: String,
        var articleUrl: String,
    ): LastFMBiography()

    object LastFMEmptyBiography: LastFMBiography()


}