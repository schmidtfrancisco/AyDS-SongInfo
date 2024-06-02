package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import ayds.songinfo.moredetails.dependencyinjector.MoreDetailsInjector
import ayds.songinfo.moredetails.domain.InfoCard
import com.squareup.picasso.Picasso


class MoreDetailsActivity: Activity() {

    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var cardDescriptionsTextView: List<TextView>
    private lateinit var cardSourceLabelsTextView: List<TextView>
    private lateinit var cardUrlButtons: List<Button>
    private lateinit var cardLogosImageView: List<ImageView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initObservers()
        getArtistCardsAsync()

    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
        moreDetailsPresenter = MoreDetailsInjector.getMoreDetailsPresenter()
    }

    private fun initProperties(){
        cardDescriptionsTextView = listOf(
            findViewById(R.id.card1DescriptionTextView),
            findViewById(R.id.card2DescriptionTextView),
            findViewById(R.id.card3DescriptionTextView)
        )

        cardSourceLabelsTextView = listOf(
            findViewById(R.id.card1SourceTextView),
            findViewById(R.id.card2SourceTextView),
            findViewById(R.id.card3SourceTextView)
        )

        cardUrlButtons = listOf(
            findViewById(R.id.card1UrlButton),
            findViewById(R.id.card2UrlButton),
            findViewById(R.id.card3UrlButton)
        )

        cardLogosImageView = listOf(
            findViewById(R.id.card1LogoImageView),
            findViewById(R.id.card2LogoImageView),
            findViewById(R.id.card3LogoImageView)
        )
    }

    private fun initObservers(){
        moreDetailsPresenter.uiStateObservable.
        subscribe{value -> updateCardsUI(value)}
    }

    private fun getArtistCardsAsync(){
        Thread{
            getArtistCards()
        }.start()
    }

    private fun getArtistCards(){
        val artistName = getArtistName()
        moreDetailsPresenter.updateCard(artistName)
    }

    private fun getArtistName() = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateCardsUI(uiState: List<CardUIState>){
        runOnUiThread {
            uiState.forEachIndexed { index, cardUIState ->
                updateCardDescription(index, cardUIState.cardDescriptionHtml)
                updateCardSource(index, cardUIState.cardSource)
                updateCardUrlButtonListener(index, cardUIState.url)
                updateCardLogoImage(index, cardUIState.logoUrl)
            }
        }
    }

    private fun updateCardDescription(index: Int, descriptionTextHtml: String){
        cardDescriptionsTextView[index].text = Html.fromHtml(descriptionTextHtml)
    }

    private fun updateCardSource(index: Int,source: InfoCard.Source?){
        cardSourceLabelsTextView[index].text = source?.name ?: ""
    }

    private fun updateCardUrlButtonListener(index: Int, url: String){
        cardUrlButtons[index].visibility = View.VISIBLE
        cardUrlButtons[index].setOnClickListener {
            navigateToUrl(url)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateCardLogoImage(index: Int, url: String){
        Picasso.get().load(url).into(cardLogosImageView[index])
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}