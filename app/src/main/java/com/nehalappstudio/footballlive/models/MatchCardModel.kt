package com.nehalappstudio.footballlive.models

data class MatchCardModel(
    val id: String,
    val date: String,
    val time: String,
    val status: String,
    val team: TeamModel,
    val venue: VenueModel,
    val duration: String,
    val league: String,
    )

data class VenueModel(
    val id: String,
    val name: String,
    val city: String
)

data class TeamModel(
    val home: SingleTeam,
    val away: SingleTeam,
)

data class SingleTeam(
    val id:String,
    val name: String,
    val logo: String,
    val winner: Boolean,
    val goal: String,
)


