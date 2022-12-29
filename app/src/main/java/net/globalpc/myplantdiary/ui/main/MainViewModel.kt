package net.globalpc.myplantdiary.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.globalpc.myplantdiary.dto.Plant
import net.globalpc.myplantdiary.service.PlantService

class MainViewModel : ViewModel() {

    private var _plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()
    internal var plants : MutableLiveData<ArrayList<Plant>>
        get() { return _plants }
        set(value) {
            _plants = value
        }

    var plantService : PlantService = PlantService()

    init {
        fetchPlants("e")
    }

    fun fetchPlants(plantName: String) {
        _plants = plantService.fetchPlants( plantName )
    }

}