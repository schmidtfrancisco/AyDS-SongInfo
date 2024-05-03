package ayds.songinfo.moredetails.fulllogic.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistBiographyEntity(
    @PrimaryKey
    val artistName: String,
    val content: String,
    val articleUrl: String
)