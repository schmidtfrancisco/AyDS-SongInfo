package ayds.songinfo.moredetails.fulllogic.data.local

import ayds.songinfo.moredetails.fulllogic.domain.Biography.ArtistBiography

class LastFMLocalStorageImpl(
    private var database: ArtistBiographyDatabase
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

