@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.abs

// Урок 3: циклы
// Максимальное количество баллов = 9
// Рекомендуемое количество баллов = 7
// Вместе с предыдущими уроками = 16/21

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая (2 балла)
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int {
    var res = 0
    var number = n
    if (number == 0) return 1
    while (number != 0) {
        res++
        number /= 10
    }
    return res
}

/**
 * Простая (2 балла)
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n) = fib(n - 2) + fib(n - 1)
 */
fun fib(n: Int): Int {
    var prev = 1
    var preprev = 1
    var i = 0
    var s = 2
    while (i < n - 2) {
        s = prev + preprev
        prev = preprev
        preprev = s
        i++
    }
    return preprev
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    if (!isPrime(n)) for (i in 2..n) if (n % i == 0) return i
    return n
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    if (!isPrime(n)) for (i in n - 1 downTo 2) if (n % i == 0) return i
    return 1
}

/**
 * Простая (2 балла)
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var steps = 0
    var num = x
    while (num != 1) {
        steps++
        if (num % 2 == 0)
            num /= 2
        else
            num = 3 * num + 1
    }
    return steps
}


fun gcd(m: Int, n: Int): Int {
    var x = m
    var y = n
    while (x != 0 && y != 0) {
        if (x > y) x %= y else y %= x
    }
    return x + y
}

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int = m * n / gcd(m, n)

/**
 * Средняя (3 балла)
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean = gcd(m, n) == 1

/**
 * Средняя (3 балла)
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {
    var x = n
    var res = 0
    while (x != 0) {
        res += x % 10
        x /= 10
        if (x != 0) res *= 10
    }
    return res
}

/**
 * Средняя (3 балла)
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean = revert(n) == n

/**
 * Средняя (3 балла)
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    val aim = n % 10
    var x = n / 10
    while (x != 0) {
        if (x % 10 != aim) return true
        x /= 10
    }
    return false
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    var res = 0.0
    var n = 1
    var tmp = 0.0
    var one = 2
    val y = x % (2 * PI)
    do {
        tmp = (-1.0).pow(one) * y.pow(n) / factorial(n)
        one++
        n += 2
        res += tmp
    } while (abs(tmp) > eps)
    return res
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    var res = 0.0
    var n = 0
    var tmp = 0.0
    var one = 2
    val y = x % (2 * PI)
    do {
        tmp = (-1.0).pow(one) * y.pow(n) / factorial(n)
        one++
        n += 2
        res += tmp
    } while (abs(tmp) > eps)
    return res
}

/**
 * Сложная (4 балла)
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 1 4 9 16 25 36 49 64 81 100 121 144 169 196 225 256 289 324 361 400
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var curTotalDigNum = 0
    var curNum = 1
    var i = 1
    var delta = -1
    while (true) {
        curNum = i * i
        curTotalDigNum += digitNumber(curNum)
        delta = curTotalDigNum - n
        //println("$n, $curNum, $curTotalDigNum, $delta")
        if (delta >= 0) return ((curNum % (10.0.pow(delta + 1).toInt()) - curNum % (10.0.pow(delta).toInt())) / 10.0.pow(delta).toInt())
        i++
    }
}

/**
 * Сложная (5 баллов)
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun fibSequenceDigit(n: Int): Int {
    var curTotalDigNum = 0
    var curNum = 1
    var i = 1
    var delta = -1
    while (true) {
        curNum = fib(i)
        curTotalDigNum += digitNumber(curNum)
        delta = curTotalDigNum - n
        //println("$n, $curNum, $curTotalDigNum, $delta")
        if (delta >= 0) return ((curNum % (10.0.pow(delta + 1).toInt()) - curNum % (10.0.pow(delta).toInt())) / 10.0.pow(delta).toInt())
        i++
    }
}

fun main() {
    println(isCoPrime(2, 1))
}