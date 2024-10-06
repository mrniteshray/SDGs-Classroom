package com.nitesh.app.sdgsclassroom

data class Goal(
    val goalName: String = "",
    val goalDescription: String = "",
    val goalImages: List<String> = listOf(),
    val audioNarrationUrl: String = ""
)

