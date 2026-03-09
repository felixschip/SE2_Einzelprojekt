package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

// Tests für die 100% Coverage für LeaderboardController
class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null)
        val body = res.body!!
        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(3, body.size)
        assertEquals(first, body[0])
        assertEquals(second, body[1])
        assertEquals(third, body[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(null)
        val body = res.body!!

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(3, body.size)
        assertEquals(first, body[0])
        assertEquals(second, body[1])
        assertEquals(third, body[2])
    }

    @Test
    fun test_getLeaderboard_withValidRank_returnsNeighbouringPlayers() {
        val results = (1..10).map { GameResult(it.toLong(), "Player$it", 100 - it, it * 1.0) }
        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(5)

        assertEquals(HttpStatus.OK, res.statusCode)
        val body = res.body!!
        //Spieler auf den Rängen 2,3,4,5,6,7,8
        assertEquals(7, body.size)
        assertEquals("Player2", body[0].playerName) // Rang 2
        assertEquals("Player3", body[1].playerName) // Rang 3
        assertEquals("Player4", body[2].playerName) // Rang 4
        assertEquals("Player5", body[3].playerName) // Rang 5
        assertEquals("Player6", body[4].playerName) // Rang 6
        assertEquals("Player7", body[5].playerName) // Rang 7
        assertEquals("Player8", body[6].playerName) // Rang 8
    }

    @Test
    fun test_getLeaderboard_withRankTooLow_returnsBadRequest() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        val res = controller.getLeaderboard(0)

        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
        assertEquals(null, res.body)
    }

    @Test
    fun test_getLeaderboard_withRankTooHigh_returnsBadRequest() {
        val results = listOf(GameResult(1, "OnlyOne", 100, 10.0))
        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(2)

        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
        assertEquals(null, res.body)
    }

}