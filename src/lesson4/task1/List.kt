@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import lesson3.task1.isPrime
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

// Урок 4: списки
// Максимальное количество баллов = 12
// Рекомендуемое количество баллов = 8
// Вместе с предыдущими уроками = 24/33

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.lowercase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая (2 балла)
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double {
    var res = 0.0
    for (element in v) res += element * element
    return sqrt(res)
}

/**
 * Простая (2 балла)
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double {
    if (list.isEmpty()) return 0.0
    var res = 0.0
    for (element in list) res += element
    return res / list.size
}

/**
 * Средняя (3 балла)
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    val clear = mean(list)
    for (i in list.indices) list[i] -= clear
    return list
}

/**
 * Средняя (3 балла)
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int {
    var res = 0
    for (i in a.indices) res += a[i] * b[i]
    return res
}

/**
 * Средняя (3 балла)
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */
fun polynom(p: List<Int>, x: Int): Int {
    var res = 0
    for (i in p.indices) res += p[i] * (x.toDouble()).pow(i).toInt()
    return res
}

/**
 * Средняя (3 балла)
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun accumulate(list: MutableList<Int>): MutableList<Int> {
    if (list.isEmpty()) return list
    var s = list[0]
    for (i in 1 until list.size) {
        s += list[i]
        list[i] = s
    }
    return list
}

/**
 * Средняя (3 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */
fun factorize(n: Int): List<Int> {
    if (isPrime(n)) return listOf(n)
    val muls = mutableListOf<Int>()
    var i = 3
    var x = n
    while (x % 2 == 0) {
        muls.add(2)
        x /= 2
    }
    while (x != 1) {
        while (!isPrime(i)) i += 2
        while (x % i == 0) {
            muls.add(i)
            x /= i
        }
        i += 2
    }
    return muls
}

/**
 * Сложная (4 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String = factorize(n).joinToString(separator = "*")

/**
 * Средняя (3 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    val res = mutableListOf<Int>()
    if (n == 0) {
        res.add(0)
        return res
    }
    var x = n
    while (x != 0) {
        res.add(x % base)
        x /= base
    }
    return res.reversed()
}

/**
 * Сложная (4 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String =
    convert(n, base).joinToString(separator = "", transform = {
        if (it < 10) "$it" else {
            val nit = 'a' + (it - 10)
            "$nit"
        }
    })

/**
 * Средняя (3 балла)
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int {
    var res = 0
    for (i in digits.size - 1 downTo 0) res += digits[digits.size - i - 1] * base.toDouble().pow(i).toInt()
    return res
}

/**
 * Сложная (4 балла)
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */
fun decimalFromString(str: String, base: Int): Int {
    var res = 0
    for (i in str.length - 1 downTo 0) {
        var x = 0
        x = if (str[i] > '9') str[i] - 'a' + 10 else str[i].digitToInt()
        res += x * base.toDouble().pow(str.length - i - 1).toInt()
    }
    return res
}

fun CharMulInt(ch: Char, n: Int): String {
    var res = ""
    for (i in 1..n) res += ch
    return res
}

/**
 * Сложная (5 баллов)
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */
fun roman(n: Int): String {
    val list = mutableListOf<Int>()
    var res = ""
    for (i in convert(n, 10)) list.add(i)
    for (i in list.indices) {
        list[i] *= 10.0.pow(list.size - i - 1).toInt()
        res += when {
            list[i] < 4 -> CharMulInt('I', list[i])
            list[i] == 4 -> "IV"
            list[i] in 5..8 -> 'V' + CharMulInt('I', list[i] - 5)
            list[i] == 9 -> "IX"
            list[i] in 10..39 -> CharMulInt('X', list[i] / 10)
            list[i] == 40 -> "XL"
            list[i] in 50..89 -> 'L' + CharMulInt('X', (list[i] - 50) / 10)
            list[i] == 90 -> "XC"
            list[i] in 100..399 -> CharMulInt('C', list[i] / 100)
            list[i] == 400 -> "CD"
            list[i] in 500..899 -> 'D' + CharMulInt('C', (list[i] - 500) / 100)
            list[i] == 900 -> "CM"
            list[i] > 900 -> CharMulInt('M', list[i] / 1000)
            else -> ""
        }
    }
    return res
}

// 123
fun russianFirstHundred(n: Int): String {
    // Turn number into list of digits
    val listHundred = convert(n, 10)

    var res = ""
    var firstDigit = 0
    var secondDigit = 0
    var thirdDigit = 0

    // Realize indices
    when (listHundred.size) {
        3 -> { // if n >= 100
            firstDigit = 0
            secondDigit = 1
            thirdDigit = 2
        }
        2 -> { // if n in 10..99
            secondDigit = 0
            thirdDigit = 1
        }
        else -> thirdDigit = 0 // if n in 0..9
    }


    if (listHundred.size == 3) {
        res += when (listHundred[firstDigit]) {
            1 -> "сто"
            2 -> "двести"
            3 -> "триста"
            4 -> "четыреста"
            5 -> "пятьсот"
            6 -> "шестьсот"
            7 -> "семьсот"
            8 -> "восемьсот"
            else -> "девятьсот"
        }
    }

    if (listHundred.size >= 2) {
        when (listHundred[secondDigit]) {
            2 -> res += "двадцать"
            3 -> res += "тридцать"
            4 -> res += "сорок"
            5 -> res += "пятьдесят"
            6 -> res += "шестьдесят"
            7 -> res += "семьдесят"
            8 -> res += "восемьдесят"
            9 -> res += "девяносто"
            0 -> res += ""
            else -> res += when (listHundred[thirdDigit]) {
                0 -> "десять"
                1 -> "одиннадцать"
                2 -> "двенадцать"
                3 -> "тринадцать"
                4 -> "четырнадцать"
                5 -> "пятнадцать"
                6 -> "шестнадцать"
                7 -> "семнадцать"
                8 -> "восемнадцать"
                else -> "девятнадцать"
            }
        }
    }
    if (listHundred.size == 1 || listHundred[secondDigit] != 1) {
        res += when (listHundred[thirdDigit]) {
            1 -> "один"
            2 -> "два"
            3 -> "три"
            4 -> "четыре"
            5 -> "пять"
            6 -> "шесть"
            7 -> "семь"
            8 -> "восемь"
            9 -> "девять"
            else -> ""
        }
    }
    return res
}

fun go(n: Int, h: Boolean): String {
    if (n == 0) return ""
    var goRes = ""
    val digits = convert(n, 10)
    var len = digits.size
    var currentDigit = 0
    while (len > 0) {
        when (len) {
            3 -> {
                goRes += when (digits[currentDigit]) {
                    9 -> "девятьсот "
                    8 -> "восемьсот "
                    7 -> "семьсот "
                    6 -> "шестьсот "
                    5 -> "пятьсот "
                    4 -> "четыреста "
                    3 -> "триста "
                    2 -> "двести "
                    else -> "сто "
                }
                len--
                currentDigit++
            }
            2 -> {
                goRes.trim()
                if (digits[currentDigit] != 1) len-- else len -= 2
                when (digits[currentDigit]) {
                    9 -> goRes += "девяносто "
                    8 -> goRes += "восемьдесят "
                    7 -> goRes += "семьдесят "
                    6 -> goRes += "шестьдесят "
                    5 -> goRes += "пятьдесят "
                    4 -> goRes += "сорок "
                    3 -> goRes += "тридцать "
                    2 -> goRes += "двадцать "
                    0 -> goRes += ""
                    else -> goRes += when (digits[currentDigit + 1]) {
                        9 -> "девятнадцать"
                        8 -> "восемнадцать"
                        7 -> "семнадцать"
                        6 -> "шестнадцать"
                        5 -> "пятнадцать"
                        4 -> "четырнадцать"
                        3 -> "тринадцать"
                        2 -> "двенадцать"
                        1 -> "одиннадцать"
                        else -> "десять"
                    }
                }
                currentDigit++
            }
            1 -> {
                goRes.trim()
                len--
                goRes += when (digits[currentDigit]) {
                    9 -> "девять"
                    8 -> "восемь"
                    7 -> "семь"
                    6 -> "шесть"
                    5 -> "пять"
                    4 -> "четыре"
                    3 -> "три"
                    2 -> if (h) "две" else "два"
                    1 -> if (h) "одна" else "один"
                    else -> ""
                }
            }
        }
    }
    return goRes.trim()
}

/**
 * Очень сложная (7 баллов)
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
fun russian(n: Int): String {
    if (n == 0) return "ноль"
    var res = ""
    val hundreds = mutableListOf<Int>()
    hundreds.add((n - n % 1000) / 1000)
    hundreds.add(n % 1000)
    if (hundreds[0] != 0) {
        res += go(hundreds[0], true)
        res += if ((hundreds[0] % 100 - hundreds[0] % 10) / 10 != 1)
            when (hundreds[0] % 10) {
                1 -> " тысяча "
                2, 3, 4 -> " тысячи "
                else -> " тысяч "
            }
        else " тысяч "
    }
    res += go(hundreds[1], false)
    return res.trim()
}

fun main() {
    println(russian(534012))
    println(russian(11))
    println(russian(313))
    println(russian(11000))
    println(russian(123456))
    println(russian(238))
    println(russian(3))
}