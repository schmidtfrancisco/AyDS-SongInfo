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
        var biographyText = ""
        var term: String? = null

        when (artistBiography){
            is ArtistBiography -> {
                biographyText = getArtistBiographyText(artistBiography)
                term = artistBiography.artistName
            }
            else -> {
                biographyText = "No results found"
            }
        }
        return textToHtml(biographyText, term)

    }

    private fun getArtistBiographyText(artistBiography: ArtistBiography): String{
        val text = (if (artistBiography.isLocallyStored) "[*]" else "") + artistBiography.content
        val parsedText = text.replace("\\n", "\n")
        return textToHtml(parsedText, artistBiography.artistName)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }
}