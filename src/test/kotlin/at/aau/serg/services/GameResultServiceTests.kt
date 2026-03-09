package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

// Tests für die 100% Coverage für GameResultService
class GameResultServiceTests {

    private lateinit var service: GameResultService

    @BeforeEach
    fun setup() {
        service = GameResultService()
    }

    @Test
    fun test_getGameResults_emptyList() {
        val result = service.getGameResults()

        assertEquals(emptyList<GameResult>(), result)
    }

    @Test
    fun test_addGameResult_getGameResults_containsSingleElement() {
        val gameResult = GameResult(1, "player1", 17, 15.3)

        service.addGameResult(gameResult)
        val res = service.getGameResults()

        assertEquals(1, res.size)
        assertEquals(gameResult, res[0])
    }

    @Test
    fun test_getGameResultById_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(1)

        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResultById_nonexistentId_returnsNull() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(22)

        assertNull(res)
    }

    @Test
    fun test_addGameResult_multipleEntries_correctId() {
        val gameResult1 = GameResult(0, "player1", 17, 15.3)
        val gameResult2 = GameResult(0, "player2", 25, 16.0)

        service.addGameResult(gameResult1)
        service.addGameResult(gameResult2)

        val res = service.getGameResults()

        assertEquals(2, res.size)

        assertEquals(gameResult1, res[0])
        assertEquals(1, res[0].id)

        assertEquals(gameResult2, res[1])
        assertEquals(2, res[1].id)
    }

    @Test
    fun test_deleteGameResult_existingId_removesElement() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val deleted = service.deleteGameResult(1)

        assertTrue(deleted)
        assertEquals(emptyList<GameResult>(), service.getGameResults())
    }

    @Test
    fun test_deleteGameResult_nonexistentId_returnsNull() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val deleted = service.deleteGameResult(99)

        assertFalse(deleted)
        assertEquals(1, service.getGameResults().size)
    }

    @Test
    fun test_deleteGameResult_multipleEntries_removesCorrectOne() {
        val result1 = GameResult(0, "p1", 10, 1.0)
        val result2 = GameResult(0, "p2", 20, 2.0)
        service.addGameResult(result1) // ID 1
        service.addGameResult(result2) // ID 2

        val deleted = service.deleteGameResult(1)

        assertTrue(deleted)
        assertEquals(listOf(result2.copy(id = 2)), service.getGameResults())
    }

}