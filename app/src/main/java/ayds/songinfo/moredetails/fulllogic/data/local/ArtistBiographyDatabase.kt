package ayds.songinfo.moredetails.fulllogic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.fulllogic.data.local.ArtistBiographyDao
import ayds.songinfo.moredetails.fulllogic.data.local.ArtistBiographyEntity

@Database(entities = [ArtistBiographyEntity::class], version = 1)
abstract class ArtistBiographyDatabase: RoomDatabase() {

    abstract fun artistBiographyDao(): ArtistBiographyDao
}