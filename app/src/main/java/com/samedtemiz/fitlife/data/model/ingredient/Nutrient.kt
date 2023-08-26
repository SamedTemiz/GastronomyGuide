package com.samedtemiz.fitlife.data.model.ingredient

data class Nutrient(
    val amount: Double,
    val name: String,
    val percentOfDailyNeeds: Double,
    val unit: String
)