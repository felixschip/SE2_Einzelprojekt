package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull


// Tests für die 100% Coverage für GameResultController
class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun `getGameResult with existing id returns game result`() {
        val id = 1L
        val expected = GameResult(id, "player", 100, 30.0)
        whenever(mockedService.getGameResult(id)).thenReturn(expected)

        val result = controller.getGameResult(id)

        assertEquals(expected, result)
        verify(mockedService).getGameResult(id)
    }

    @Test
    fun `getGameResult with non-existing id returns null`() {
        val id = 99L
        whenever(mockedService.getGameResult(id)).thenReturn(null)

        val result = controller.getGameResult(id)

        assertNull(result)
        verify(mockedService).getGameResult(id)
    }

    @Test
    fun `getAllGameResults returns list from service`() {
        val expectedList = listOf(
            GameResult(1, "p1", 100, 10.0),
            GameResult(2, "p2", 90, 12.0)
        )
        whenever(mockedService.getGameResults()).thenReturn(expectedList)

        val result = controller.getAllGameResults()

        assertEquals(expectedList, result)
        verify(mockedService).getGameResults()
    }

    @Test
    fun `addGameResult calls service add`() {
        val gameResult = GameResult(0, "newPlayer", 50, 20.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun `deleteGameResult calls service delete`() {
        val id = 5L

        controller.deleteGameResult(id)

        verify(mockedService).deleteGameResult(id)
    }
}