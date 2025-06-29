package hr.foi.zavrsniapp.data.models

data class NbaGameResponse(
    val Game: NbaGame
)

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
    val LastPlay: String?,
    val SeriesInfo: SeriesInfo?,
    val Quarters: List<Quarter>?
)

data class SeriesInfo(
    val HomeTeamWins: Int,
    val AwayTeamWins: Int,
    val GameNumber: Int,
    val MaxLength: Int
)

data class Quarter(
    val QuarterID: Int,
    val GameID: Int,
    val Number: Int,
    val Name: String,
    val AwayScore: Int,
    val HomeScore: Int
)