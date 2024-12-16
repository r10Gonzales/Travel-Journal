package com.pmb.traveljournal.models

data class TravelNote(
    var id: String = "",  // ID for Firebase
    var title: String = "",
    var description: String = "",
    var location: String = "",
    var date: String = ""
)

