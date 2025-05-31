package hr.foi.zavrsniapp.data.models

data class NbaGame(
    val GameID: Int,
    val Season: Int,
    val SeasonType: Int,
    val Status: String,
    val Day: String,
    val DateTime: String?,
    val AwayTeam: String,
    val HomeTeam: String,
    val AwayTeamScore: Int?,
    val HomeTeamScore: Int?,
    val Quarter: String?,
    val TimeRemainingMinutes: Int?,
    val TimeRemainingSeconds: Int?,
)