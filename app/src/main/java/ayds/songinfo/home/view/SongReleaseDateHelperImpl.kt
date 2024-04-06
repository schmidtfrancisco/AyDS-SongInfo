package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song

private const val YEAR = 0
private const val MONTH = 1
private const val DAY = 2

interface SongReleaseDateHelper {
    fun getSongReleaseDate(song: Song.SpotifySong): String
}

internal class SongReleaseDateHelperImpl: SongReleaseDateHelper{

    override fun getSongReleaseDate(song: Song.SpotifySong): String {
        val date = song.releaseDate.split('-')
        return when (song.releaseDatePrecision) {
            "day" -> getDateDay(date)
            "month" -> getDateMonth(date)
            "year" -> getDateYear(date)
            else -> "unknown release date"
        }
    }

    private fun getDateDay(date: List<String>): String {
        return "${date[DAY]}/${date[MONTH]}/${date[YEAR]}"
    }


    private fun getDateMonth(date: List<String>): String {
        val monthName = getMonthName(date[MONTH].toInt())
        return "$monthName, " + date[YEAR]

    }

    private fun getDateYear(date: List<String>): String {
        val txtLeapYear = leapYear(date[YEAR].toInt())
        return date[YEAR] + " ($txtLeapYear)"
    }

    private fun getMonthName(monthNumber: Int): String =
        when(monthNumber){
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month number"
        }

    private fun leapYear(year: Int): String =
        if (isLeapYear(year))
            "a leap year"
        else
            "not a leap year"

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}