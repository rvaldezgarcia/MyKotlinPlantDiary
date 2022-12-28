package net.globalpc.myplantdiary.dto

import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("genus")    var genus : String,
    @SerializedName("species")  var species: String,
    @SerializedName("common")   var common: String,
    @SerializedName("id")       var plantID: Int = 0
) {

    override fun toString(): String {
        return common
    }
}