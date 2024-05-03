package ayds.songinfo.moredetails.fulllogic.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.fulllogic.dependencyinjector.MoreDetailsPresenterInjector
import ayds.songinfo.moredetails.fulllogic.dependencyinjector.MoreDetailsViewInjector
import com.squareup.picasso.Picasso


private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"


class MoreDetailsActivity: Activity() {

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMLogoImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initObservers()
        getArtistInfo()
        showLogoImage()
    }

    private fun initModule(){
        MoreDetailsViewInjector.init(this)
        moreDetailsPresenter = MoreDetailsPresenterInjector.getMoreDetailsPresenter()
    }

    private fun initProperties(){
        articleTextView = findViewById(R.id.articleTextView)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMLogoImageView = findViewById(R.id.lastFMLogoImageView)
    }

    private fun initObservers(){
        moreDetailsPresenter.uiStateObservable.
            subscribe{value -> updateBiographyInfo(value)}
    }

    private fun getArtistInfo(){
        moreDetailsPresenter.manageEvent(MoreDetailsUIEvent.OpenWindow)
    }

    private fun updateBiographyInfo(uiState: MoreDetailsUIState){
        runOnUiThread {
            updateArtistBiographyText(uiState.biographyText)
            updateOpenUrlButtonListener(uiState.articleUrl)
        }
    }

    private fun updateArtistBiographyText(biographyText: String){
        articleTextView.text = Html.fromHtml(biographyText)
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

    private fun showLogoImage(){
        runOnUiThread {
            Picasso.get().load(LASTFM_LOGO_URL).into(lastFMLogoImageView)
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}