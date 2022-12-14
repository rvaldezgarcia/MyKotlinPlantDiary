package net.globalpc.myplantdiary.service

import androidx.lifecycle.MutableLiveData
import net.globalpc.myplantdiary.dao.IPlantDAO
import net.globalpc.myplantdiary.dto.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantService {

    fun fetchPlants(plantName : String) : MutableLiveData<ArrayList<Plant>> {

        var _plants = MutableLiveData<ArrayList<Plant>>()

        val service = RetrofitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)

        val call = service?.getAllPlants()

        call?.enqueue( object: Callback<ArrayList<Plant>> {

            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {
                val j = 1 + 1;
                val i = 1 + 1;
            }

            override fun onResponse(
                call: Call<ArrayList<Plant>>,
                response: Response<ArrayList<Plant>>
            ) {
                _plants.value = response.body()
            }
        })

        return _plants
    }
}