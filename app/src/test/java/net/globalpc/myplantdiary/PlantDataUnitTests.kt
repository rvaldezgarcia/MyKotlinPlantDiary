package net.globalpc.myplantdiary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
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
class PlantDataUnitTests {

    @get:Rule
    var rule : TestRule = InstantTaskExecutorRule()

    lateinit var mvm : MainViewModel
    
    var plantService = mockk<PlantService>()

    @Test
    fun confirmEasternRedbud_outputsEasternRedbud () {
        var plant: Plant = Plant("Cercis", "canadensis", "Eastern Redbud")
        assertEquals( "Eastern Redbud",  plant.toString() );
    }

    @Test
    fun searchForRedbud_returnsRedbud() {

        givenAFeedOfMockedPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainsEasternRedbud()
    }

    private fun givenAFeedOfMockedPlantDataAreAvailable() {
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {

        var allPlantsLiveData = MutableLiveData<ArrayList<Plant>>()
        var allPlants = ArrayList<Plant>()

        // Create and add plants to our collection.
        var redbud = Plant( "Cercis", "canadensis", "Eastern Redbud")
        allPlants.add(redbud)
        // var redOak = Plant("Quercus", "rubra", "Red Oak")
        // allPlants.add(redOak)
        var whiteOak = Plant("Quercus", "alba", "White Oak")
        allPlants.add(whiteOak)
        var englishOak = Plant("Quercus", "alba", "English Oak")
        allPlants.add(englishOak)
        allPlantsLiveData.postValue(allPlants)

        every {
            plantService.fetchPlants( or( "Redbud","Quercus" ) ) // plantService.fetchPlants( any<String>() ) // plantService.fetchPlants( "sklujapouetllkjsdau" )
        } returns allPlantsLiveData

        every {
            plantService.fetchPlants( not( or( "Redbud","Quercus" ) ) ) // plantService.fetchPlants( any<String>() ) // plantService.fetchPlants( "sklujapouetllkjsdau" )
        } returns MutableLiveData<ArrayList<Plant>>()

        mvm.plantService = plantService
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
        }

        assertTrue(redbudFound)
    }

    @Test
    fun searchForQuercus_returnsEnglishOakAndWhiteOak() {

        givenAFeedOfMockedPlantDataAreAvailable()
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
        }

        assertTrue(whiteOakFound)
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
        }

        assertTrue(englishOakFound)
    }

    @Test
    fun searchForGarbage_returnsNothing() {

        givenAFeedOfMockedPlantDataAreAvailable()
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