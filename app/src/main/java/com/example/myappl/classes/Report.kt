package com.example.myappl.classes

data class Report(
    val titre: String,
    val description: String = "",
    val imageUrl: String = "",
    val category: String,
    val location: String = "",
    val timestamp: Long = 0
) {



}