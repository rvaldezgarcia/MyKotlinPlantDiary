package net.globalpc.myplantdiary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.globalpc.myplantdiary.dto.Plant
import net.globalpc.myplantdiary.service.PlantService
import net.globalpc.myplantdiary.ui.main.MainViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlantDataIntegrationTest {

    @get:Rule
    var rule : TestRule = InstantTaskExecutorRule()

    lateinit var mvm : MainViewModel

    // var plantService = mockk<PlantService>()

    @Test
    fun confirmEasternRedbud_outputsEasternRedbud () {
        var plant: Plant = Plant("Cercis", "canadensis", "Eastern Redbud")
        assertEquals( "Eastern Redbud",  plant.toString() );
    }

    @Test
    fun searchForRedbud_returnsRedbud() {

        givenAFeedOfPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainsEasternRedbud()
    }

    private fun givenAFeedOfPlantDataAreAvailable() {
        mvm = MainViewModel()
    }

    private fun whenSearchForRedbud() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenResultContainsEasternRedbud() {

        var redbudFound : Boolean = false

        mvm.plants.observeForever {
            // here is where we do the observing
            assertNotNull(it)
            assertTrue( it.size > 0 )
            it.forEach {
                if( it.genus == "Cercis" && it.species == "canadensis" && it.common.contains("Eastern Redbud") ) {
                    redbudFound = true
                }
            }

            assertTrue(redbudFound)
        }
    }

    @Test
    fun searchForQuercus_returnsEnglishOakAndWhiteOak() {

        givenAFeedOfPlantDataAreAvailable()
        whenSearchForQuercus()
        thenResultContainsEnglishOak()
        thenResultContainsWhiteOak()
    }

    private fun whenSearchForQuercus() {
        mvm.fetchPlants("Quercus")
    }

    private fun thenResultContainsWhiteOak() {

        var whiteOakFound = false

        mvm.plants.observeForever {
            // here is where we do the observing
            assertNotNull(it)
            assertTrue( it.size > 0 )
            it.forEach {
                if( it.genus == "Quercus" && it.species == "alba" && it.common.contains("White Oak") ) {
                    whiteOakFound = true
                }
            }

            assertTrue(whiteOakFound)
        }
    }

    private fun thenResultContainsEnglishOak() {

        var englishOakFound = false

        mvm.plants.observeForever {
            // here is where we do the observing
            assertNotNull(it)
            assertTrue( it.size > 0 )
            it.forEach {
                if( it.genus == "Quercus" && it.species == "alba" && it.common.contains("English Oak") ) {
                    englishOakFound = true
                }
            }

            assertTrue(englishOakFound)
        }
    }

    @Test
    fun searchForGarbage_returnsNothing() {

        givenAFeedOfPlantDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResults()
    }

    private fun whenISearchForGarbage() {
        mvm.fetchPlants("sklujapouetllkjsdau")
    }

    private fun thenIGetZeroResults() {
        mvm.plants.observeForever {
            assertEquals( 0, it.size )
        }
    }
}