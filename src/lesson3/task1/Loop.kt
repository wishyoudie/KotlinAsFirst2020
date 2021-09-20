@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.*

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
    var count = 0
    var num = n
    do {
        count++
        num /= 10
    } while (num > 0)
    return count
}

/**
 * Простая (2 балла)
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var valueActual = 1
    var valuePrevious = 1
    var valueInter: Int
    if (n == 1) {
        return valuePrevious
    } else if (n == 2) {
        return valueActual
    } else {
        for (i in 3..n) {
            valueInter = valueActual
            valueActual += valuePrevious
            valuePrevious = valueInter
        }
        return valueActual
    }
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    var divider = 2
    do {
        if (n % divider == 0) break
        divider++
    } while (n != divider)
    return divider
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    var divider = n - 1
    do {
        if (n % divider == 0) break
        divider--
    } while (n != divider)
    return divider
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
    var nextValue = x
    var steps = 0
    while (nextValue != 1) {
        if (nextValue % 2 == 0) {
            nextValue /= 2
        } else {
            nextValue = 3 * nextValue + 1
        }
        steps++
    }
    return steps
}

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int {
    var k = 1
    for (i in 1..(m * n)) {
        if ((k % m == 0) && (k % n == 0)) {
            break
        } else {
            k++
        }
    }
    return k
}

/**
 * Средняя (3 балла)
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean {
    for (i in 2..maxOf(m, n)) {
        if ((m % i != 0) || (n % i != 0)) {
            continue
        } else {
            return false
        }
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {
    var number: Int = n
    var newNumber = 0.0
    var numberLenght = 0
    n.toDouble()
    do {
        number /= 10
        numberLenght++
    } while (number > 0)
    number = n
    for (i in 1..numberLenght) {
        newNumber += (number % 10.0) * 10.0.pow(numberLenght - i)
        number /= 10
    }
    return newNumber.toInt()
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
fun isPalindrome(n: Int): Boolean {
    var number: Int = n
    var numberLenght = 0
    do {
        number /= 10
        numberLenght++
    } while (number > 0)
    number = n
    for (i in 1..numberLenght step 2) {
        if (number % 10 == (number / 10.0.pow(numberLenght - i)).toInt()) {
            number = (number % 10.0.pow(numberLenght - i) / 10.0).toInt()
            continue
        } else {
            return false
        }
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    var number: Int = n
    var numberPrevious: Int = n % 10
    var numberCurrent: Int
    var numberLenght = 0
    do {
        number /= 10
        numberLenght++
    } while (number > 0)
    number = n
    for (i in 1..numberLenght) {
        numberCurrent = number % 10
        if (numberCurrent == numberPrevious) {
            numberPrevious = numberCurrent
            number /= 10
            continue
        } else {
            return true
        }
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
    var sinValue = 0.0
    var sign = 1
    for (i in 1..50 step 2) {
        sinValue += ((x % (2 * PI)).pow(i) / factorial(i)) * sign
        if (abs(((x % (2 * PI)).pow(i) / factorial(i)) * sign) - eps < 0) {
            break
        } else {
            sign *= -1
            continue
        }
    }
    return sinValue
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
    var cosValue = 1.0
    var sign: Int = -1
    for (i in 2..150 step 2) {
        cosValue += ((x % (2 * PI)).pow(i) / factorial(i)) * sign
        if (abs(((x % (2 * PI)).pow(i) / factorial(i)) * sign) - eps < 0) {
            break
        } else {
            sign *= -1
            continue
        }
    }
    return cosValue
}

/**
 * Сложная (4 балла)
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var expectedPos: Int = n
    var number = 1
    var numberSquare: Int
    var numberSquareLenght = 0
    do {
        numberSquare = number * number
        do {
            numberSquare /= 10
            numberSquareLenght++
        } while (numberSquare > 0)
        numberSquare = number * number
        expectedPos -= numberSquareLenght
        if (expectedPos > 0) {
            number++
            numberSquareLenght = 0
            continue
        } else {
            break
        }
    } while (true)
    return (numberSquare / 10.0.pow(abs(expectedPos)) % 10).toInt()
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
    var expectedPos: Int = n
    var numberCurrent = 1
    var numberCurrentDouble: Int
    var numberPrevious = 1
    var valueInter: Int
    var numberCurrentLenght = 0
    if (n == 1) {
        return numberPrevious
    } else if (n == 2) {
        return numberCurrent
    } else {
        expectedPos -= 2
        do {
            valueInter = numberCurrent
            numberCurrent += numberPrevious
            numberPrevious = valueInter
            numberCurrentDouble = numberCurrent
            do {
                numberCurrentDouble /= 10
                numberCurrentLenght++
            } while (numberCurrentDouble > 0)
            expectedPos -= numberCurrentLenght
            if (expectedPos > 0) {
                numberCurrentLenght = 0
                continue
            } else {
                break
            }
        } while (true)
        return (numberCurrent / 10.0.pow(abs(expectedPos)) % 10).toInt()
    }
}
