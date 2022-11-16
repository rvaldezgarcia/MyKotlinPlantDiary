package net.globalpc.myplantdiary.dto

data class Plant( var genus : String, var species: String, var common: String ) {

    override fun toString(): String {
        return common
    }
}