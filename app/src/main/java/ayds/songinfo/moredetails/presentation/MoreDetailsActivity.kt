package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.songinfo.moredetails.domain.InfoCard
import com.squareup.picasso.Picasso


class MoreDetailsActivity: Activity() {

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var articleTextView: TextView
    private lateinit var sourceLabelTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMLogoImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initObservers()
        getArtistInfoAsync()
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
        moreDetailsPresenter = MoreDetailsInjector.getMoreDetailsPresenter()
    }

    private fun initProperties(){
        articleTextView = findViewById(R.id.articleTextView)
        sourceLabelTextView = findViewById(R.id.cardTextView)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMLogoImageView = findViewById(R.id.lastFMLogoImageView)
    }

    private fun initObservers(){
        moreDetailsPresenter.uiStateObservable.
        subscribe{value -> updateCardInfo(value)}
    }

    private fun getArtistInfoAsync(){
        Thread{
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo(){
        val artistName = getArtistName()
        moreDetailsPresenter.getArtistInfo(artistName)
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateCardInfo(uiState: MoreDetailsUIState){
        runOnUiThread {
            updateCardDescription(uiState.cardDescriptionHtml)
            updateCardSource(uiState.cardSource)
            updateOpenUrlButtonListener(uiState.articleUrl)
            updateLogoImage(uiState.logoUrl)
        }
    }

    private fun updateCardDescription(biographyTextHtml: String){
        articleTextView.text = Html.fromHtml(biographyTextHtml)
    }

    private fun updateCardSource(source: InfoCard.Source?){
        sourceLabelTextView.text = source?.name ?: ""
    }

    private fun updateOpenUrlButtonListener(articleUrl: String){
        openUrlButton.setOnClickListener {
            navigateToUrl(articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLogoImage(url: String){
        Picasso.get().load(url).into(lastFMLogoImageView)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}