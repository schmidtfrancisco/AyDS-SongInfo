package ayds.songinfo.moredetails.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.data.local.ArtistBiographyDao
import ayds.songinfo.moredetails.data.local.ArtistBiographyEntity

@Database(entities = [ArtistBiographyEntity::class], version = 1)
abstract class ArtistBiographyDatabase: RoomDatabase() {

    abstract fun artistBiographyDao(): ArtistBiographyDao
}