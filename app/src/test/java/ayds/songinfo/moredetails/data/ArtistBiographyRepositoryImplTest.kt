package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.LastFMExternalService
import ayds.songinfo.moredetails.data.local.LastFMLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiographyRepository
import ayds.songinfo.moredetails.domain.Biography
import ayds.songinfo.moredetails.domain.Biography.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue


class ArtistBiographyRepositoryImplTest {
    private val lastFMLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMExternalService: LastFMExternalService = mockk(relaxUnitFun = true)

    private val artistBiographyRepository: ArtistBiographyRepository = ArtistBiographyRepositoryImpl(
        lastFMLocalStorage,
        lastFMExternalService
    )

    @Test
    fun `given existing artist biography should return it and mark it as local`(){
        val artistBiography = ArtistBiography("artistName", "content", "url", false)
        every { lastFMLocalStorage.getBiographyByArtistName("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertTrue(artistBiography.isLocallyStored)
    }

    @Test
    fun `given non-existing local biography should get the biography with content and store it `(){
        val artistBiography = ArtistBiography("artistName", "content", "url", false)
        every { lastFMLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        verify { lastFMLocalStorage.insertArtistBiography(artistBiography) }

    }

    @Test
    fun `given non-existing local biography should get the biography with no content and not store it`(){
        val artistBiography = ArtistBiography("artistName", LastFMExternalService.NO_CONTENT, "url", false)
        every { lastFMLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns artistBiography

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(artistBiography, result)
        assertFalse(artistBiography.isLocallyStored)
        verify(inverse = true) { lastFMLocalStorage.insertArtistBiography(artistBiography) }
    }

    @Test
    fun `given non-existing artist biography it should return EmptyBiography`(){
        every { lastFMLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } returns null

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(Biography.EmptyBiography, result)
    }

    @Test
    fun `given service exception it should return EmptyBiography`(){
        every { lastFMLocalStorage.getBiographyByArtistName("artistName") } returns null
        every { lastFMExternalService.getArtistBiography("artistName") } throws mockk<Exception>()

        val result = artistBiographyRepository.getBiographyByArtistName("artistName")

        assertEquals(Biography.EmptyBiography, result)
    }

}