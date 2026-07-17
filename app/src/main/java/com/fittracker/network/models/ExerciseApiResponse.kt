package com.fittracker.network.models

import com.google.gson.annotations.SerializedName

data class ExerciseApiResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("bodyPart") val bodyPart: String,
    @SerializedName("equipment") val equipment: String,
    @SerializedName("gifUrl") val gifUrl: String?,
    @SerializedName("target") val target: String,
    @SerializedName("instructions") val instructions: List<String>?
)
