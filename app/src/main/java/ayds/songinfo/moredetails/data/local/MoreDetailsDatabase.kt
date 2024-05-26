package ayds.songinfo.moredetails.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class], version = 1)
abstract class MoreDetailsDatabase: RoomDatabase() {

    abstract fun cardDao(): CardDao
}