package board

import java.lang.IllegalArgumentException

open class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val board = Array(width) { Array(width) { Cell(0, 0) } }

    init {
        (1..width).forEach { row ->
            (1..width).forEach { column ->
                board[row - 1][column - 1] = Cell(row, column)
            }
        }
    }

    private fun isSafe(i: Int, j: Int): Boolean {
        return i in 1..width && j in 1..width
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (isSafe(i, j)) {
            board[i - 1][j - 1]
        } else {
            null
        }
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (!isSafe(i, j))
            throw IllegalArgumentException("$i and $j must be in range 1 to $width")
        return board[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        val list = mutableListOf<Cell>()
        (1..width).forEach { row ->
            (1..width).forEach { column ->
                list.add(board[row - 1][column - 1])
            }
        }
        return list
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val list = mutableListOf<Cell>()
        jRange.iterator().forEach { column ->
            val cell = getCellOrNull(i, column)
            if (cell != null) {
                list.add(cell)
            }
        }
        return list
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val list = mutableListOf<Cell>()
        iRange.iterator().forEach { row ->
            val cell = getCellOrNull(row, j)
            if (cell != null) {
                list.add(cell)
            }
        }
        return list
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(this.i - 1, this.j)
            Direction.DOWN -> getCellOrNull(this.i + 1, this.j)
            Direction.LEFT -> getCellOrNull(this.i, this.j - 1)
            Direction.RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }

}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellMap = mutableMapOf<Cell, T?>()

    init {
        getAllCells().forEach { cell ->
            cellMap[cell] = null
        }
    }

    override fun get(cell: Cell): T? = cellMap[cell]

    override fun set(cell: Cell, value: T?) {
        cellMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellMap.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellMap.filterValues(predicate).keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellMap.filterValues(predicate).keys.isNotEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellMap.filterValues(predicate).keys.size == getAllCells().size
    }

    override fun print() {
        var idx = 0
        println("==============================")
        cellMap.forEach {
            if (idx % width == 0)
                println()
            print(it.value)
            print("\t")
            idx++
        }
        println()
        println("==============================")
    }

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)