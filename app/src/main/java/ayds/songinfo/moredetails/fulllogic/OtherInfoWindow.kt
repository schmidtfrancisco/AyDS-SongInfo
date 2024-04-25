package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale


private const val ARTICLE_BD_NAME = "database-article"
private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

private const val BIOGRAPHY = "bio"
private const val ARTIST = "artist"
private const val CONTENT = "content"
private const val ARTICLE_URL = "url"
private const val NO_RESULTS = "No Results"

data class ArtistBiography(var artistName: String, var content: String, var articleUrl: String)

class OtherInfoWindow : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMLogoImageView:  ImageView

    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initProperties()
        initDatabase()
        initLastFMAPI()
        getArtistInfoAsync()
        showLogoImage()
    }

    private fun initProperties(){
        articleTextView = findViewById(R.id.articleTextView)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMLogoImageView = findViewById(R.id.lastFMLogoImageView)
    }

    private fun initDatabase(){
        articleDatabase = databaseBuilder(
            this,
            ArticleDatabase::class.java,
            ARTICLE_BD_NAME
        ).build()
    }

    private fun initLastFMAPI(){
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistBiography = getArtistBiographyFromRepository()
        updateUi(artistBiography)
    }

    private fun getArtistBiographyFromRepository(): ArtistBiography {
        val artistName = getArtistName()
        val dbArticle = getArticleFromDB(artistName)
        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        }
        else {
            artistBiography = getArticleFromService(artistName)

            if (artistBiography.content.isNotEmpty()) {
                insertArticleInDB(artistBiography)
            }
        }
        return artistBiography
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val articleEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return when {
            articleEntity != null -> ArtistBiography(artistName, "[*]" + articleEntity.biography, articleEntity.articleUrl)
            else -> null
        }

    }

    private fun ArtistBiography.markItAsLocal() = copy(content = "[*]$content")

    private fun getArticleFromService(artistName: String): ArtistBiography {
        var artistBiography = ArtistBiography("", "", "")
        try {
            val callResponse = getArtistInfoFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return artistBiography
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArtistBiography {
        val gson = Gson()
        val jsonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jsonObject[ARTIST].getAsJsonObject()
        val biography = artist[BIOGRAPHY].getAsJsonObject()
        val extract = biography[CONTENT]
        val url = artist[ARTICLE_URL]
        var biographyContent: String = extract?.asString ?: NO_RESULTS

        if (extract != null) {
            biographyContent = extract.asString.replace("\\n", "\n")
            biographyContent = textToHtml(biographyContent, artistName)
        }

        return ArtistBiography(artistName, biographyContent, url.asString)
    }

    private fun insertArticleInDB(artistBiography: ArtistBiography){
        articleDatabase.ArticleDao().insertArticle(ArticleEntity(artistBiography.artistName, artistBiography.content, artistBiography.articleUrl))
    }

    private fun updateUi(artistBiography: ArtistBiography){
        runOnUiThread {
            updateArtistBiographyText(artistBiography)
            updateOpenUrlButtonAction(artistBiography)
        }
    }
    private fun updateArtistBiographyText(artistBiography: ArtistBiography){
        val text = artistBiography.content.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, artistBiography.artistName))

    }
    private fun updateOpenUrlButtonAction(artistBiography: ArtistBiography){
        openUrlButton.setOnClickListener {
            navigateToUrl(artistBiography.articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun showLogoImage(){
        runOnUiThread {
            Picasso.get().load(LASTFM_LOGO_URL).into(lastFMLogoImageView)
        }
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
