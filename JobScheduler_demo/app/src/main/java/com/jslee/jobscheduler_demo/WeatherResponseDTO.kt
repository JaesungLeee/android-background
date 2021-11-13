package com.jslee.jobscheduler_demo

import com.google.gson.annotations.SerializedName

data class WeatherResponseDTO(
    @SerializedName("cod") var code: Int = 0,
    @SerializedName("main") var main: Main? = null,
    @SerializedName("sys") var sys: Sys? = null
) {
    data class Main (
        @SerializedName("temp") var temp: Float = 0.toFloat(),
        @SerializedName("temp_max") var maxTemp: Float = 0.toFloat(),
        @SerializedName("temp_min") var minTemp: Float = 0.toFloat()
    )

    data class Sys(
        @SerializedName("country") var country: String? = null
    )
}
