package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(value = "rank", required = false) rank: Int?): ResponseEntity<List<GameResult>> {
        // sortierung nach score (hoch nach niedrig) und nach kürzerer gametime
        val sortedgameresultlist = gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        if(rank == null){
            return ResponseEntity.ok(sortedgameresultlist)
        }
        if (rank < 1 || rank > sortedgameresultlist.size) {
            // HTTP:400 bei ungültigem rank wert
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        // 3 über und 3 unter dem angegebenen Rang ausgeben
        val startIndex = (rank - 4).coerceAtLeast(0)
        val endIndex = (rank + 2).coerceAtMost(sortedgameresultlist.size - 1) + 1
        val subList = sortedgameresultlist.subList(startIndex, endIndex)
        return ResponseEntity.ok(subList)
    }
}