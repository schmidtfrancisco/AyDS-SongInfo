package ayds.songinfo.moredetails.fulllogic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ayds.songinfo.moredetails.fulllogic.data.local.ArtistBiographyEntity

@Dao
interface ArtistBiographyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtistBiography(article: ArtistBiographyEntity)

    @Query("SELECT * FROM Artistbiographyentity WHERE artistName LIKE :artistName LIMIT 1")
    fun getBiographyByArtistName(artistName: String): ArtistBiographyEntity?
}