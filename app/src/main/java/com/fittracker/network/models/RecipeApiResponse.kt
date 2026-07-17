package com.fittracker.network.models

import com.google.gson.annotations.SerializedName

data class RecipeApiResponse(
    @SerializedName("label") val label: String,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("calories") val calories: Double,
    @SerializedName("totalWeight") val totalWeight: Double?,
    @SerializedName("ingredientLines") val ingredientLines: List<String>?,
    @SerializedName("totalNutrients") val nutrients: NutrientMap?
)

data class NutrientMap(
    @SerializedName("ENERC_KCAL") val energy: NutrientDetail?,
    @SerializedName("PROCNT") val protein: NutrientDetail?,
    @SerializedName("FAT") val fat: NutrientDetail?,
    @SerializedName("CHOCDF") val carbs: NutrientDetail?
)

data class NutrientDetail(
    @SerializedName("label") val label: String,
    @SerializedName("quantity") val quantity: Double,
    @SerializedName("unit") val unit: String
)
