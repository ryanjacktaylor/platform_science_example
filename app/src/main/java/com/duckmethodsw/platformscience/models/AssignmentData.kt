package com.duckmethodsw.platformscience.models

import com.google.gson.annotations.SerializedName

data class AssignmentData(
    @SerializedName("drivers") val drivers: List<String>,
    @SerializedName("shipments") val shipments: List<String>
)