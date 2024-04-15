package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song

private const val YEAR = 0
private const val MONTH = 1
private const val DAY = 2

interface ReleaseDateHelperFactory{
    fun getReleaseDateHelper(song: Song.SpotifySong): ReleaseDateHelper
}

class ReleaseDateHelperFactoryImpl: ReleaseDateHelperFactory{
    override fun getReleaseDateHelper(song: Song.SpotifySong): ReleaseDateHelper {
        return when (song.releaseDatePrecision) {
            "day" -> ReleaseDateDayHelper(song)
            "month" -> ReleaseDateMonthHelper(song)
            "year" -> ReleaseDateYearHelper(song)
            else -> ReleaseDateDefaultHelper(song)
        }
    }
}

interface ReleaseDateHelper {
    val song: Song.SpotifySong
    fun getSongReleaseDate(): String
}

internal class ReleaseDateDayHelper(override val song: Song.SpotifySong): ReleaseDateHelper{
    override fun getSongReleaseDate(): String {
        val date = song.releaseDate.split('-')
        return "${date[DAY]}/${date[MONTH]}/${date[YEAR]}"
    }
}

internal class ReleaseDateMonthHelper(override val song: Song.SpotifySong): ReleaseDateHelper{
    override fun getSongReleaseDate(): String {
        val date = song.releaseDate.split('-')
        val monthName = getMonthName(date[MONTH].toInt())
        return "$monthName, " + date[YEAR]
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
}

internal class ReleaseDateYearHelper(override val song: Song.SpotifySong): ReleaseDateHelper{
    override fun getSongReleaseDate(): String {
        val txtLeapYear = leapYear(song.releaseDate.toInt())
        return song.releaseDate + " ($txtLeapYear)"
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

internal class ReleaseDateDefaultHelper(override val song: Song.SpotifySong): ReleaseDateHelper{
    override fun getSongReleaseDate(): String {
        return song.releaseDate
    }
}
