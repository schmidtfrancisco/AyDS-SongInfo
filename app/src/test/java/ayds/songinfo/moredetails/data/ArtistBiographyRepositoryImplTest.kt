package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMExternalService
import ayds.songinfo.moredetails.data.local.MoreDetailsLocalStorage
import ayds.songinfo.moredetails.domain.MoreDetailsRepository
import ayds.songinfo.moredetails.domain.Card
import ayds.songinfo.moredetails.domain.Card.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue


class ArtistBiographyRepositoryImplTest {
    private val moreDetailsLocalStorage: MoreDetailsLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMExternalService: LastFMExternalService = mockk(relaxUnitFun = true)

    private val artistBiographyRepository: MoreDetailsRepository = ArtistBiographyRepositoryImpl(
        moreDetailsLocalStorage,
        lastFMExternalService
    )

    @Test
    fun `given existing artist biography should return it and mark it as local`(){
        val artistBiography = ArtistBiography("artistName", "content", "url", false)
        every { moreDetailsLocalStorage.getBiographyByArtistName("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertTrue(artistBiography.isLocallyStored)
    }

    @Test
    fun `given non-existing local biography should get the biography with content and store it `(){
        val artistBiography = ArtistBiography("artistName", "content", "url", false)
        every { moreDetailsLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        verify { moreDetailsLocalStorage.insertArtistBiography(artistBiography) }

    }

    @Test
    fun `given non-existing local biography should get the biography with no content and not store it`(){
        val artistBiography = ArtistBiography("artistName", LastFMExternalService.NO_CONTENT, "url", false)
        every { moreDetailsLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        verify(inverse = true) { moreDetailsLocalStorage.insertArtistBiography(artistBiography) }
    }

    @Test
    fun `given non-existing artist biography it should return EmptyBiography`(){
        every { moreDetailsLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns null

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(Card.EmptyBiography, result)
    }

    @Test
    fun `given service exception it should return EmptyBiography`(){
        every { moreDetailsLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } throws mockk<Exception>()

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(Card.EmptyBiography, result)
    }

}