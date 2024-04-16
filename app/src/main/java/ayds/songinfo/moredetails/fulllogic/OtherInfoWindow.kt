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
private const val LOGO_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindow : Activity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var artistName: String

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

        Thread {
            dataBase.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase.ArticleDao().getArticleByArtistName("nada"))
        }.start()
    }

    private fun initLastFMAPI(){
        val retrofit = Retrofit.Builder()
            .baseUrl(AUDIOSCROBBLER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun showArtistInfo(){
        val artist = intent.getStringExtra(ARTIST_NAME_EXTRA)
        if (artist != null) {
            artistName = artist
            getArtistInfo()
        }
        else
            updateArtistInfoText("No Results")
    }

    private fun getArtistInfo() = Thread {
        var article = dataBase.ArticleDao().getArticleByArtistName(artistName)
        when {
            article != null -> updateLocalArtistInfo(article)

            else -> {
                try {
                    article = getArticleFromService()

                    if (article != null){
                        updateExternalArtistInfo(article)
                        dataBase.ArticleDao().insertArticle(article)
                    }
                    else
                        updateArtistInfoText("No Results")

                } catch (e: IOException) {
                    Log.e("TAG", "Error $e")
                    e.printStackTrace()
                }
            }
        }
    }.start()

    private fun getArticleFromService(): ArticleEntity? {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        return jsonToArticle(callResponse.body())
    }

    private fun jsonToArticle(serviceData: String?): ArticleEntity? {
        Log.e("TAG", "JSON $serviceData")
        val gson = Gson()
        val jsonObject = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jsonObject["artist"].getAsJsonObject()
        val biography = artist["bio"].getAsJsonObject()
        val extract = biography["content"]
        val url = artist["url"]

        return when {
            extract != null -> {
                var biographyContent = extract.asString.replace("\\n", "\n")
                biographyContent = textToHtml(biographyContent, artistName)

                ArticleEntity(artistName, biographyContent, url.asString)
            }
            else -> null
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

    private fun updateLocalArtistInfo(article: ArticleEntity){
        updateArtistInfoText("[*]" + article.biography)
        updateButtonAction(article.articleUrl)
    }

    private fun updateExternalArtistInfo(article: ArticleEntity){
        updateArtistInfoText(article.biography)
        updateButtonAction(article.articleUrl)
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
        Log.e("TAG", "Get Image from $LOGO_IMAGE_URL")
        runOnUiThread {
            Picasso.get().load(LOGO_IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"

    }
}
