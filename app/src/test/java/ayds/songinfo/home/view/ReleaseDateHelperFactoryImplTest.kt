package ayds.songinfo.home.view

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateHelperFactoryImplTest {
    private val releaseDateResolverFactory: ReleaseDateHelperFactory =
        ReleaseDateHelperFactoryImpl()

    @Test
    fun `on day precision should return day precision resolver`() {
        val result = releaseDateResolverFactory.getReleaseDateHelper(mockk {
            every { releaseDatePrecision } returns "day"
        })


        assertEquals(result::class.java, ReleaseDateDayHelper::class.java)
    }

    @Test
    fun `on month precision should return month precision resolver`() {
        val result = releaseDateResolverFactory.getReleaseDateHelper(mockk {
            every { releaseDatePrecision } returns "month"
        })

        assertEquals(result::class.java, ReleaseDateMonthHelper::class.java)
    }

    @Test
    fun `on year precision should return year precision resolver`() {
        val result = releaseDateResolverFactory.getReleaseDateHelper(mockk {
            every { releaseDatePrecision } returns "year"
        })

        assertEquals(result::class.java, ReleaseDateYearHelper::class.java)
    }

}
