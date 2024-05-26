package ayds.songinfo.moredetails.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ayds.songinfo.moredetails.domain.InfoCard

@Entity
data class CardEntity(
    @PrimaryKey
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: InfoCard.Source
)

