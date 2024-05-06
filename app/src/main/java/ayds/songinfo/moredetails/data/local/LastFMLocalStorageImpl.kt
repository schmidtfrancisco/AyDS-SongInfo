package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.Biography.ArtistBiography

class LastFMLocalStorageImpl(
    database: ArtistBiographyDatabase
): LastFMLocalStorage {

    private val artistBiographyDao: ArtistBiographyDao = database.artistBiographyDao()

    override fun insertArtistBiography(artistBiography: ArtistBiography) {
        artistBiographyDao.insertArtistBiography(artistBiography.toArtistBiographyEntity())
    }

    override fun getBiographyByArtistName(artistName: String): ArtistBiography? {
        return artistBiographyDao.getBiographyByArtistName(artistName)?.toArtistBiography()
    }

    private fun ArtistBiography.toArtistBiographyEntity() = ArtistBiographyEntity(
        this.artistName,
        this.content,
        this.articleUrl
    )

    private fun ArtistBiographyEntity.toArtistBiography() = ArtistBiography(
        this.artistName,
        this.content,
        this.articleUrl
    )
}

