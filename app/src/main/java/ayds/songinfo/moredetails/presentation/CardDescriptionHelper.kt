package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.InfoCard
import ayds.songinfo.moredetails.domain.InfoCard.EmptyCard
import ayds.songinfo.moredetails.domain.InfoCard.Card
import java.util.Locale

interface CardDescriptionHelper{
    fun getCardText(infoCard: InfoCard = EmptyCard): String
}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

internal class CardDescriptionHelperImpl: CardDescriptionHelper{

    override fun getCardText(infoCard: InfoCard): String {

        val htmlDescriptionText = when (infoCard){
            is Card -> {
                getCardDescription(infoCard)

            }
            else -> {
                val descriptionText = "No results found"
                val term = "No results"
                textToHtml(descriptionText, term)
            }
        }
        return htmlDescriptionText

    }

    private fun getCardDescription(card: Card): String{
        val text = (if (card.isLocallyStored) "[*]" else "") + card.description
        return textToHtml(text, card.artistName)
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        val textWithBold = text
            .replace("'", "")
            .replace("\\n", "\n")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }
}