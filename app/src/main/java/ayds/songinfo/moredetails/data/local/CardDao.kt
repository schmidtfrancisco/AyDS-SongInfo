package ayds.songinfo.moredetails.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity)

    @Query("SELECT * FROM CardEntity WHERE artistName LIKE :artistName LIMIT 3")
    fun getCardByArtistName(artistName: String): List<CardEntity>
}