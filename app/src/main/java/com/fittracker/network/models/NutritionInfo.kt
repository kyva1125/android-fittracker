package com.fittracker.network.models

import com.google.gson.annotations.SerializedName

data class NutritionInfo(
    @SerializedName("calories") val calories: Double,
    @SerializedName("protein") val protein: Double, // in grams
    @SerializedName("carbohydrates") val carbs: Double, // in grams
    @SerializedName("fat") val fat: Double // in grams
)
