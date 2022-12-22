package net.globalpc.myplantdiary.service

import androidx.lifecycle.MutableLiveData
import net.globalpc.myplantdiary.dto.Plant

class PlantService {

    fun fetchPlants(plantName : String) : MutableLiveData<ArrayList<Plant>> {

        return MutableLiveData<ArrayList<Plant>>()
    }
}