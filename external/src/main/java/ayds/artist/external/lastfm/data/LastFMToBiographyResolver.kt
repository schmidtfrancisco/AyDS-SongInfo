package ayds.artist.external.lastfm.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import ayds.artist.external.lastfm.data.LastFMBiography.LastFMArtistBiography

interface LastFMToBiographyResolver{
    fun getArtistBioFromExternalData(serviceData: String?, artistName: String): LastFMArtistBiography?

}

private const val BIOGRAPHY = "bio"
private const val ARTIST = "artist"
private const val CONTENT = "content"
private const val ARTICLE_URL = "url"


internal class JsonToBiographyResolver: LastFMToBiographyResolver {

    override fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): LastFMArtistBiography? =
        serviceData?.getJsonObject()?.let {bio ->
            LastFMArtistBiography(artistName, bio.getContent(), bio.getArticleUrl() )
        }

    private fun String?.getJsonObject(): JsonObject {
        val gson = Gson()
        return gson.fromJson(this, JsonObject::class.java)
    }

    private fun JsonObject.getContent(): String {
        val artist = this[ARTIST].getAsJsonObject()
        val biography = artist[BIOGRAPHY].getAsJsonObject()
        val extract = biography[CONTENT]
        return extract?.asString ?: LastFMService.NO_CONTENT
    }

    private fun JsonObject.getArticleUrl(): String {
        val artist = this[ARTIST].getAsJsonObject()
        return artist[ARTICLE_URL].asString
    }


}