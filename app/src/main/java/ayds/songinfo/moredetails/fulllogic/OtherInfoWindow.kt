package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val AUDIOSCROBBLER_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val BIOGRAPHY = "bio"
private const val ARTIST = "artist"
private const val CONTENT = "content"
private const val ARTICLE_URL = "url"
private const val NO_RESULTS = "No Results"

class OtherInfoWindow : Activity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        artistInfoTextView = findViewById(R.id.textPane1)

        initDatabase()
        initLastFMAPI()
        showArtistInfo()
        showLogoImage()
    }

    private fun initDatabase(){
        dataBase = databaseBuilder(
            this,
            ArticleDatabase::class.java,
            "article-database"
        ).build()
    }

    private fun initLastFMAPI(){
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun showArtistInfo(){
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        if (artistName != null) {
            getArtistInfo(artistName)
        }
    }

    private fun getArtistInfo(artistName: String) = Thread {
        var article = dataBase.ArticleDao().getArticleByArtistName(artistName)
        when {
            article != null -> updateLocalArtistInfo(article)

            else -> {
                article = getArticleFromService(artistName)

                article?.let {
                    if (it.biography != NO_RESULTS) {
                        dataBase.ArticleDao().insertArticle(it)
                    }
                }
                updateExternalArtistInfo(article)
            }
        }
    }.start()

    private fun getArticleFromService(artistName: String): ArticleEntity? =
        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            jsonToArticle(callResponse.body(), artistName)
        }catch (e: IOException){
            Log.e("TAG", "Error $e")
            e.printStackTrace()
            null
        }


    private fun jsonToArticle(serviceData: String?, artistName: String): ArticleEntity {
        Log.e("TAG", "JSON $serviceData")
        val gson = Gson()
        val jsonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jsonObject[ARTIST].getAsJsonObject()
        val biography = artist[BIOGRAPHY].getAsJsonObject()
        val extract = biography[CONTENT]
        val url = artist[ARTICLE_URL]

        var biographyContent: String = NO_RESULTS

        if (extract != null) {
            biographyContent = extract.asString.replace("\\n", "\n")
            biographyContent = textToHtml(biographyContent, artistName)
        }

        return ArticleEntity(artistName, biographyContent, url.asString)

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

    private fun updateLocalArtistInfo(article: ArticleEntity){
        updateArtistInfoText("[*]" + article.biography)
        updateButtonAction(article.articleUrl)
    }

    private fun updateExternalArtistInfo(article: ArticleEntity?){
        when {
            article != null -> {
                updateArtistInfoText(article.biography)
                updateButtonAction(article.articleUrl)
            }
            else -> {
                updateArtistInfoText(NO_RESULTS)
            }
        }
    }

    private fun updateArtistInfoText(artistText: String){
        runOnUiThread {
            artistInfoTextView.text = Html.fromHtml(artistText)
        }
    }
    private fun updateButtonAction(urlString: String){
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
    }
    private fun showLogoImage(){
        Log.e("TAG", "Get Image from $LASTFM_LOGO_URL")
        runOnUiThread {
            Picasso.get().load(LASTFM_LOGO_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"

    }
}
