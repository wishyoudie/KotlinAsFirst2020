@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.abs
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {
    private constructor(c: MutableList<Double>) : this(*c.toDoubleArray())

    private val c = coeffs.toList()

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = c[c.size - i - 1]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var res = 0.0
        for (i in c.indices) res += c[i] * x.pow(c.size - 1 - i)
        return res
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int {
        for (d in c.indices) if (c[d] != 0.0) return c.size - 1 - d
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val diff = this.c.size - other.c.size
        val bigger = if (diff >= 0) this.c else other.c
        val smaller = if (diff < 0) this.c else other.c
        val newc = mutableListOf<Double>()
        for (i in 0 until abs(diff)) newc.add(bigger[i])
        for (i in 0 until bigger.size - abs(diff)) newc.add(bigger[i + abs(diff)] + smaller[i])
        return Polynom(newc)
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val newc = mutableListOf<Double>()
        for (t in c) newc.add(-t)
        return Polynom(newc)
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = this + other.unaryMinus()

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val left = mutableListOf<Double>()
        val right = mutableListOf<Double>()
        val res = mutableListOf<Double>()
        val resdeg = (this.c.size - 1) * (other.c.size - 1)

        for (i in 0 until resdeg) {
            res.add(0.0)
            if (i < this.c.size)
                left.add(this.c[i])
            else
                left.add(0.0)
            if (i < other.c.size)
                right.add(other.c[i])
            else
                right.add(0.0)
        }
        for (i in res.indices) {
            var s = 0.0
            for (r in i downTo 0) {
                s += right[r] * left[i - r]
            }
            res[i] = s
        }
        return Polynom(res)
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Polynom &&
            (this - other).c.all { it == 0.0 }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = this.c.hashCode()
}
