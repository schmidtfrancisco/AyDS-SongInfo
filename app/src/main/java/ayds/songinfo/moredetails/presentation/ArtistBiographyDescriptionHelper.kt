package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.Biography
import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import ayds.songinfo.moredetails.domain.Biography.EmptyBiography
import java.util.Locale

interface ArtistBiographyDescriptionHelper{
    fun getBiographyText(artistBiography: Biography = EmptyBiography): String
}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

internal class ArtistBiographyDescriptionHelperImpl: ArtistBiographyDescriptionHelper{

    override fun getBiographyText(artistBiography: Biography): String {

        val htmlBiographyText = when (artistBiography){
            is ArtistBiography -> {
                getArtistBiographyText(artistBiography)

            }
            else -> {
                val biographyText = "No results found"
                val term = "No results"
                textToHtml(biographyText, term)
            }
        }
        return htmlBiographyText

    }

    private fun getArtistBiographyText(artistBiography: ArtistBiography): String{
        val text = (if (artistBiography.isLocallyStored) "[*]" else "") + artistBiography.content
        return textToHtml(text, artistBiography.artistName)
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