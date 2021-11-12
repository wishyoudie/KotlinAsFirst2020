@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> =
    if (height <= 0 || width <= 0) throw IllegalArgumentException("") else MatrixImpl(height, width, e)

fun <E> createMatrix(height: Int, width: Int, values: List<List<E>>): Matrix<E> {
    val matrix = createMatrix(height, width, values[0][0])
    for (row in 0 until height) {
        for (column in 0 until width) {
            matrix[row, column] = values[row][column]
        }
    }
    return matrix
}

/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(
    override val height: Int, override val width: Int,
    e: E
) : Matrix<E> {
    private val values = mutableMapOf<Cell, E>()

    init {
        for (h in 0 until height)
            for (w in 0 until width)
                values[Cell(h, w)] = e
    }

    override fun get(row: Int, column: Int): E =
        values[Cell(row, column)] ?: throw IndexOutOfBoundsException("No such cell")

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        if (values[Cell(row, column)] != null)
            values[Cell(row, column)] = value
        else
            throw IndexOutOfBoundsException("No such cell")
    }

    override fun set(cell: Cell, value: E) {
        set(cell.row, cell.column, value)
    }

    override fun equals(other: Any?) =
        other is MatrixImpl<*> &&
                height == other.height &&
                width == other.width &&
                values == other.values

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        for (h in 0 until height - 1) {
            sb.append("[")
            for (w in 0 until width - 1) {
                sb.append(values[Cell(h, w)]!!)
                sb.append(", ")
            }
            sb.append(values[Cell(h, width - 1)]!!)
            sb.append("], ")
        }
        sb.append("[")
        for (w in 0 until width - 1) {
            sb.append(values[Cell(height - 1, w)]!!)
            sb.append(", ")
        }
        sb.append(values[Cell(height - 1, width - 1)]!!)
        sb.append("]]")
        return "$sb"
    }


    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + values.hashCode()
        return result
    }
}