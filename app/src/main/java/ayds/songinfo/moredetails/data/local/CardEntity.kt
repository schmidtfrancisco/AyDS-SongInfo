package ayds.songinfo.moredetails.data.local

import androidx.room.Entity
import ayds.songinfo.moredetails.domain.InfoCard

@Entity(primaryKeys = ["artistName", "source"])
data class CardEntity(
    val artistName: String,
    val description: String,
    val infoUrl: String,
    val source: InfoCard.Source,
    val logoUrl: String
)

