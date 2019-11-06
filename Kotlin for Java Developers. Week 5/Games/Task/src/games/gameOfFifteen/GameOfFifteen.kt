package games.gameOfFifteen

import board.Cell
import board.Direction
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val values = initializer.initialPermutation

        var k = 0
        for (i in 1..board.width) {
            for (j in 1..board.width) {
                board[board.getCell(i, j)] = if (k < values.size) values[k++] else null
            }
        }
    }

    override fun canMove() = true

    override fun hasWon(): Boolean {
        var won = true
        var idx = 1
        for (i in 1..board.width) {
            for (j in 1..board.width) {
                if (get(i, j) != idx && idx != board.width * board.width) {
                    won = false
                    break
                }
                idx++
            }
        }
        return won && get(board.width, board.width) == null
    }

    override fun processMove(direction: Direction) {
        val zero = checkNull()

        var nonZero: Cell?
        board.apply { nonZero = zero.getNeighbour(direction.reverse()) }
        nonZero?.let {
            board[zero] = board[it]
            board[it] = null
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    private fun Direction.reverse(): Direction = when (this) {
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT -> Direction.LEFT
    }

    private fun checkNull(): Cell = board.getAllCells().find { get(it.i, it.j) == null }!!
}

