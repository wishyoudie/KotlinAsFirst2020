@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.Matrix
import lesson9.task1.createMatrix
import kotlin.math.*
import java.util.PriorityQueue

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 4, width = 4:
 *  1  2  3  4
 * 12 13 14  5
 * 11 16 15  6
 * 10  9  8  7
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val res = createMatrix(height, width, 0)
    var i = 0
    var j = 0
    var k = 1
    val list = (1..height * width).toList()
    res[i, j] = 1
    do {
        while (j + 1 < width && res[i, j + 1] == 0) {
            res[i, j + 1] = list[k++]
            j++
        }
        while (i + 1 < height && res[i + 1, j] == 0) {
            res[i + 1, j] = list[k++]
            i++
        }
        while (j > 0 && res[i, j - 1] == 0) {
            res[i, j - 1] = list[k++]
            j--
        }
        while (i > 0 && res[i - 1, j] == 0) {
            res[i - 1, j] = list[k++]
            i--
        }
    } while ((j + 1 < width && res[i, j + 1] == 0) || (i + 1 < height && res[i + 1, j] == 0))
    return res
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val res = createMatrix(height, width, 0)
    var i = 0
    var j = 0
    res[i, j] = 1
    var cycle = 0
    do {
        cycle++
        while (j + 1 < width && res[i, j + 1] == 0) {
            res[i, j + 1] = cycle
            j++
        }
        while (i + 1 < height && res[i + 1, j] == 0) {
            res[i + 1, j] = cycle
            i++
        }
        while (j > 0 && res[i, j - 1] == 0) {
            res[i, j - 1] = cycle
            j--
        }
        while (i > 0 && res[i - 1, j] == 0) {
            res[i - 1, j] = cycle
            i--
        }
    } while ((j + 1 < width && res[i, j + 1] == 0) || (i + 1 < height && res[i + 1, j] == 0))
    return res
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> {
    val res = createMatrix(height, width, 0)
    var k = 0
    var i = 0
    var j = 0
    val list = (1..height * width).toList()
    if (height == 1 && width == 1) return createMatrix(1, 1, 1)

    fun findRight(matr: Matrix<Int>): Pair<Int, Int> {
        for (h in 0 until matr.height)
            for (w in 0 until matr.width)
                if (matr[h, w] == 0) return Pair(h, w)
        return Pair(-1, -1)
    }

    res[i, j] = list[k++]
    while (list[k] != height * width) {
        j = findRight(res).second
        i = findRight(res).first
        while (i < height && j >= 0) {
            res[i, j] = list[k++]
            i++
            j--
        }

    }
    res[height - 1, width - 1] = height * width

    return res
}

/**
 * Средняя (3 балла)
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width != matrix.height) throw IllegalArgumentException("Not quadratic matrix")
    val res = createMatrix(matrix.height, matrix.width, matrix[0, 0])
    for (i in 0 until matrix.height)
        for (j in 0 until matrix.width)
            res[i, j] = matrix[matrix.width - j - 1, i]
    return res
}

/**
 * Сложная (5 баллов)
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    if (matrix.width != matrix.height) return false
    val ok = (1..matrix.width).toSet()
    for (i in 0 until matrix.width) {
        val r = mutableSetOf<Int>()
        val w = mutableSetOf<Int>()
        for (j in 0 until matrix.height) {
            r.add(matrix[i, j])
            w.add(matrix[j, i])
        }
        if (r != ok || w != ok) return false
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> {
    if (matrix.width == 1 && matrix.height == 1) return createMatrix(1, 1, 0)
    val res = createMatrix(matrix.height, matrix.width, 0)
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            var s = 0

            try {
                s += matrix[i - 1, j]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i, j - 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i - 1, j - 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i + 1, j]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i, j + 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i + 1, j + 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i + 1, j - 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            try {
                s += matrix[i - 1, j + 1]
            } catch (e: IndexOutOfBoundsException) {
            }
            res[i, j] = s
        }
    }
    return res
}

/**
 * Средняя (4 балла)
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val columns = mutableListOf<Int>()
    for (h in 0 until matrix.height) {
        val cur = mutableListOf<Int>()
        for (w in 0 until matrix.width)
            cur.add(matrix[h, w])
        if (cur.all { it == 0 }) rows.add(h)
    }
    for (w in 0 until matrix.width) {
        val cur = mutableListOf<Int>()
        for (h in 0 until matrix.height)
            cur.add(matrix[h, w])
        if (cur.all { it == 0 }) columns.add(w)
    }
    return Holes(rows, columns)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя (3 балла)
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> {
    val res = createMatrix(matrix.height, matrix.width, 0)
    for (h in 0 until matrix.height) {
        for (w in 0 until matrix.width) {
            var s = 0
            for (nh in 0..h)
                for (nw in 0..w)
                    s += matrix[nh, nw]
            res[h, w] = s
        }
    }
    return res
}

/**
 * Простая (2 балла)
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    val res = this
    for (h in 0 until this.height)
        for (w in 0 until this.width)
            res[h, w] = -this[h, w]
    return res
}

/**
 * Средняя (4 балла)
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    if (this.width != other.height) throw IllegalArgumentException("Incompatible matrices")
    val res = createMatrix(this.height, other.width, 0)
    for (i in 0 until this.height)
        for (j in 0 until other.width) {
            var s = 0
            for (r in 0 until this.width)
                s += this[i, r] * other[r, j]
            res[i, j] = s
        }
    return res
}

/**
 * Сложная (7 баллов)
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    val newKey = createMatrix(key.height, key.width, 0)
    for (h in 0 until key.height)
        for (w in 0 until key.width)
            if (key[h, w] == 0) newKey[h, w] = 1
    if (key.width == lock.width && key.height == lock.height)
        return if (newKey == lock) Triple(true, 0, 0)
        else
            Triple(false, 0, 0)
    var keyX = 0
    var keyY = 0
    val keyCode = mutableListOf<Int>()
    for (h in 0 until newKey.height)
        for (w in 0 until newKey.width)
            keyCode.add(newKey[h, w])
    do {
        do {
            val lockCode = mutableListOf<Int>()
            for (h in keyY until newKey.height + keyY)
                for (w in keyX until newKey.width + keyX)
                    lockCode.add(lock[h, w])
            if (keyCode == lockCode) return Triple(true, keyY, keyX)
            keyX++
        } while (keyX - 1 != (lock.width - newKey.width))
        keyX = 0
        keyY++
    } while (keyY - 1 != (lock.height - newKey.height))
    return Triple(false, -1, -1)
}

fun Matrix<Int>.find(key: Int): Pair<Int, Int> {
    for (h in 0 until this.height)
        for (w in 0 until this.width)
            if (this[h, w] == key) return Pair(h, w)
    throw IllegalStateException("No such value")
}

fun Matrix<Int>.swap(a: Int, b: Int) {
    val acoords = this.find(a)
    val bcoords = this.find(b)
    if (!((bcoords.first == acoords.first + 1 && bcoords.second == acoords.second) ||
                (bcoords.first == acoords.first && bcoords.second == acoords.second - 1) ||
                (bcoords.first == acoords.first && bcoords.second == acoords.second + 1) ||
                (bcoords.first == acoords.first - 1 && bcoords.second == acoords.second))
    ) throw IllegalStateException("$a move is not allowed")
    this[bcoords.first, bcoords.second] = a
    this[acoords.first, acoords.second] = b
}

fun Matrix<Int>.toList(): MutableList<Int> {
    val res = mutableListOf<Int>()
    for (h in 0 until this.height)
        for (w in 0 until this.width)
            res.add(this[h, w])
    return res
}

/**
 * Сложная (8 баллов)
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    for (move in moves)
        matrix.swap(move, 0)
    return matrix
}

fun calculateDistance(a: Int, b: Int) = abs(a % 4 - b % 4) + abs(a / 4 - b / 4)

class Chain(private val state: List<Int>, private val history: List<Int> = listOf<Int>()) {

    fun getState() = this.state.toString()
    fun getHistory() = this.history

    private fun manhattan(): Int {
        var res = 0
        for (i in 0 until 16) if (state[i] != 0) res += calculateDistance((this.state[i] - 1) % 16, i)
        return res
    }

    private fun linear(): Int {
        var res = 0
        for (row in 0 until 4) {
            val r = row * 4
            for (i in 0 until 3)
                for (j in i + 1 until 4) {
                    if ((this.state[i + r] - 1) / 4 == row && (this.state[j + r] - 1) / 4 == row && this.state[i + r] > this.state[j + r]) {
                        res++
                    }
                }
        }
        for (col in 0 until 4) {
            for (i in 0 until 3) {
                val qi = 4 * i
                for (j in i + 1 until 4) {
                    val qj = 4 * j
                    if ((this.state[qi + col] - 1) % 4 == col && (this.state[qj + col] - 1) % 4 == col && this.state[qi + col] > this.state[qj + col]) {
                        res++
                    }
                }
            }
        }
        return 2 * res
    }

    /*private fun heuristic(): Int {
        val row = listOf(3, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3)
        val col = listOf(3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2)
        var res = 0
        for (i in 0 until 16)
            if (state[i] != 0)
                res += abs(row[state[i]] - i / 4) + abs(col[state[i]] - i % 4)
        return res
    }*/

    fun h() = manhattan() + linear()
    fun g() = history.size
    fun f() = g() + h()

    fun getNeighbours(): List<Chain> {
        val neighs = mutableListOf<Chain>()
        val zero_coord = state.indexOf(0)

        if (zero_coord < 15 && calculateDistance(zero_coord, zero_coord + 1) == 1) {
            val new_state = state.toMutableList()
            new_state[zero_coord + 1] = new_state[zero_coord].also { new_state[zero_coord] = new_state[zero_coord + 1] }
            neighs.add(Chain(new_state, this.history + new_state[zero_coord]))
        }

        if (zero_coord >= 1 && calculateDistance(zero_coord, zero_coord - 1) == 1) {
            val new_state = state.toMutableList()
            new_state[zero_coord - 1] = new_state[zero_coord].also { new_state[zero_coord] = new_state[zero_coord - 1] }
            neighs.add(Chain(new_state, this.history + new_state[zero_coord]))
        }

        if (zero_coord < 12 && calculateDistance(zero_coord, zero_coord + 4) == 1) {
            val new_state = state.toMutableList()
            new_state[zero_coord + 4] = new_state[zero_coord].also { new_state[zero_coord] = new_state[zero_coord + 4] }
            neighs.add(Chain(new_state, this.history + new_state[zero_coord]))
        }
        if (zero_coord >= 4 && calculateDistance(zero_coord, zero_coord - 4) == 1) {
            val new_state = state.toMutableList()
            new_state[zero_coord - 4] = new_state[zero_coord].also { new_state[zero_coord] = new_state[zero_coord - 4] }
            neighs.add(Chain(new_state, this.history + new_state[zero_coord]))
        }
        return neighs
    }

    override fun toString(): String {
        var i = 0
        val sb = StringBuilder()
        while (i < 16) {
            sb.append("${state[i]}  ")
            if (i % 4 == 3) {
                sb.append("\n")
            }
            i++
        }
        return "$sb"
    }
}

fun a_star(start: Chain, finish: Chain): Chain {
    val nodes = mutableMapOf<String, Int>()
    val chainHeap = PriorityQueue<Chain>(compareBy { it.f() })
    val goal = finish.getState()

    chainHeap.add(start)
    while (chainHeap.isNotEmpty()) {
        val currentChain = chainHeap.poll()
        val currentNode = currentChain.getState()
        if (currentNode == goal)
            return currentChain
        nodes[currentNode] = currentChain.g()
        for (ch in currentChain.getNeighbours()) {
            if (ch.getState() in nodes) {
                if (ch.g() >= nodes[ch.getState()]!!)
                    continue
                nodes[ch.getState()] = ch.g()
            }
            chainHeap.add(ch)
        }
    }
    return Chain(listOf(-1))
}

/**
 * Очень сложная (32 балла)
 */
fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    if (matrix.width != 4 || matrix.height != 4) throw IllegalArgumentException("Not 4x4 matrix")

    val input = matrix.toList()
    var n = input.indexOf(0) / 4 + 1
    for (i in 1..15) {
        var k = 0
        for (j in input.indexOf(i) until 16)
            if (input[j] != 0 && input[j] < i)
                k++
        n += k
    }
    if (n % 2 != 0) input[input.indexOf(14)] = 15.also { input[input.indexOf(15)] = 14 }
    val result = a_star(Chain(input), Chain(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0))).getHistory()
        .toMutableList()
    if (n % 2 != 0)
        for (i in result.indices)
            when (result[i]) {
                14 -> result[i] = 15
                15 -> result[i] = 14
                else -> {
                }
            }
    return result
}