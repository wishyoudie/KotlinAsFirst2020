@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "Величина с размерностью".
 *
 * Предназначен для представления величин вроде "6 метров" или "3 килограмма"
 * Общая сложность задания - средняя, общая ценность в баллах -- 18
 * Величины с размерностью можно складывать, вычитать, делить, менять им знак.
 * Их также можно умножать и делить на число.
 *
 * В конструктор передаётся вещественное значение и строковая размерность.
 * Строковая размерность может:
 * - либо строго соответствовать одной из abbreviation класса Dimension (m, g)
 * - либо соответствовать одной из приставок, к которой приписана сама размерность (Km, Kg, mm, mg)
 * - во всех остальных случаях следует бросить IllegalArgumentException
 */
class DimensionalValue(value: Double, dimension: String) : Comparable<DimensionalValue> {
    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double

    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension

    init {
        if (dimension.length == 1) {
            this.value = value
            this.dimension = when (dimension) {
                "g" -> Dimension.GRAM
                "m" -> Dimension.METER
                else -> throw IllegalArgumentException("'$dimension' is not supported")
            }
        } else {
            if (dimension.length != 2) throw IllegalArgumentException("'$dimension' is not supported")
            val c = when (dimension[0]) {
                'K' -> DimensionPrefix.KILO
                'm' -> DimensionPrefix.MILLI
                else -> throw IllegalArgumentException("'$dimension' is not supported")
            }
            this.value = value * c.multiplier
            this.dimension = when (dimension[1]) {
                'g' -> Dimension.GRAM
                'm' -> Dimension.METER
                else -> throw IllegalArgumentException("'$dimension' is not supported")
            }
        }
    }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */
    constructor(s: String) : this(s.split(" ")[0].toDouble(), s.split(" ")[1])

    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue =
        if (this.dimension != other.dimension) throw IllegalArgumentException("Different dimensions")
        else DimensionalValue(this.value + other.value, this.dimension.abbreviation)

    /**
     * Смена знака величины
     */
    operator fun unaryMinus(): DimensionalValue = DimensionalValue(-this.value, this.dimension.abbreviation)

    /**
     * Вычитание другой величины. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun minus(other: DimensionalValue): DimensionalValue = this + (-other)

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue =
        DimensionalValue(this.value * other, this.dimension.abbreviation)

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue =
        DimensionalValue(this.value / other, this.dimension.abbreviation)

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double =
        if (this.dimension != other.dimension) throw IllegalArgumentException("Different dimensions")
        else this.value / other.value

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is DimensionalValue &&
            this.dimension == other.dimension && this.value == other.value

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
    override fun compareTo(other: DimensionalValue): Int =
        if (this.dimension != other.dimension) throw IllegalArgumentException("Different dimensions")
        else when {
            this.value < other.value -> -1
            this.value > other.value -> 1
            else -> 0
        }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + dimension.hashCode()
        return result
    }
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");
}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001);
}