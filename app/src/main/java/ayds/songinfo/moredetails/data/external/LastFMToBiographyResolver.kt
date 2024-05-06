package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject


interface LastFMToBiographyResolver{
    fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArtistBiography?


}

private const val BIOGRAPHY = "bio"
private const val ARTIST = "artist"
private const val CONTENT = "content"
private const val ARTICLE_URL = "url"


internal class JsonToBiographyResolver: LastFMToBiographyResolver {

    override fun getArtistBioFromExternalData(serviceData: String?,
        artistName: String
    ): ArtistBiography? =
        try {
            serviceData?.getJsonObject()?.let {bio ->
                ArtistBiography(artistName, bio.getContent(), bio.getArticleUrl() )
            }
        }catch (e: Exception){
            null
        }

    private fun String?.getJsonObject(): JsonObject {
        val gson = Gson()
        return gson.fromJson(this, JsonObject::class.java)
    }

    private fun JsonObject.getContent(): String {
        val artist = this[ARTIST].getAsJsonObject()
        val biography = artist[BIOGRAPHY].getAsJsonObject()
        val extract = biography[CONTENT]
        return extract?.asString ?: LastFMExternalService.NO_CONTENT
    }

    private fun JsonObject.getArticleUrl(): String {
        val artist = this[ARTIST].getAsJsonObject()
        return artist[ARTICLE_URL].asString
    }


}