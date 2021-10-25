@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import kotlin.NumberFormatException
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val parts = str.split(" ")
    val preres = mutableListOf<Int>()
    var res = ""
    val months = listOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )
    // check input
    if (parts.size != 3 || parts[1] !in months) return res
    preres += parts[0].toInt()
    for (i in months.indices) if (parts[1] == months[i]) preres += i + 1
    preres += parts[2].toInt()
    // check logic
    if (preres[0] > daysInMonth(preres[1], preres[2])) return res
    if (preres[0] < 10) res += "0"
    res += preres[0].toString() + "."
    if (preres[1] < 10) res += "0"
    res += preres[1].toString() + "."
    res += preres[2].toString()
    return res
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val parts = digital.split(".")
    // check input
    if (parts.size != 3) return ""
    try {
        val partsInt = mutableListOf<Int>()

        for (i in parts) partsInt += i.toInt()
        // check logic
        if (partsInt[0] > 31 || partsInt[1] > 12 || partsInt[1] < 1 || partsInt[0] > daysInMonth(
                partsInt[1],
                partsInt[2]
            )
        )
            return ""
        val months = listOf(
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"
        )
        return partsInt[0].toString() + " " + months[partsInt[1] - 1] + " " + parts[2]
    } catch (e: NumberFormatException) {
        return ""
    }
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    var res = ""
    var addPlus = true
    var i = 0
    val ok = " +-()"
    val digs = "0123456789"
    var flag = false

    if ('(' in phone && ')' in phone) {
        var s = phone.substringBefore(')')
        s = s.substringAfter('(')
        flag = false
        for (j in s) if (j in digs) flag = true
        if (!flag) return ""
    }
    for (ch in digs) if (ch in phone) {
        flag = true
        break
    }
    if (!flag) return ""
    while (i != phone.length) {
        if (phone[i] !in (ok + digs)) return ""
        if (phone[i] == '+' && addPlus) {
            res += '+'
            addPlus = false
        }
        if (phone[i] in digs) res += phone[i]
        i++
    }
    return res
}

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    val attempts = jumps.split(" ")
    var maxJump = -1
    val ok = "-%0123456789"
    for (attempt in attempts) {
        if (attempt.length == 1 && attempt !in ok) return -1
        try {
            maxJump = max(maxJump, attempt.toInt())
        } catch (e: NumberFormatException) {
            continue
        }
    }
    return maxJump
}


fun checkIfNotOk(str: String): Boolean {
    val ok = "+-%"
    for (ch in str) if (ch !in ok) return true
    return false
}

/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val attempts = mutableMapOf<Int, String>()
    val tmp = jumps.split(" ")
    for (i in 0..(tmp.size - 2) step 2) {
        try {
            attempts[tmp[i].toInt()] = tmp[i + 1]
            if (checkIfNotOk(tmp[i + 1])) return -1
        } catch (e: NumberFormatException) {
            return -1
        }
    }
    var maxJump = -1
    for (j in attempts.keys) if (attempts[j]!![attempts[j]!!.length - 1] == '+') maxJump = max(maxJump, j)

    return maxJump
}

fun safeToInt(s: String): Int {
    val ok = "0123456789"
    for (i in s) if (i !in ok) throw IllegalArgumentException("Bad operation")
    return s.toInt()
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (expression.isEmpty()) throw IllegalArgumentException("Empty expression")
    val ok = "0123456789 "
    val ops = "+-"
    for (check in expression) if (check !in (ok + ops)) throw IllegalArgumentException("$check is not allowed in expression")
    if (" " !in expression) return safeToInt(expression)
    val commands = expression.split(" ")
    for (i in commands.indices) {
        if ((i % 2 == 0 && commands[i] in ops) || (i % 2 != 0 && commands[i] !in ops)) throw IllegalArgumentException("Bad expression format")
    }
    var res = commands[0].toInt()
    for (i in 1..(commands.size - 2) step 2) {
        val x = commands[i + 1].toInt()
        if (x < 0) throw IllegalArgumentException("Non-positive number in $expression")
        when (commands[i]) {
            "+" -> res += x
            "-" -> res -= x
        }
    }
    return res
}

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val words = str.split(" ")
    var res = 0
    var prev = words[0].lowercase()
    var index = -1

    for (i in 1 until words.size) {
        val word = words[i].lowercase()
        if (prev == word) {
            index = i - 1
            break
        }
        prev = word
    }
    if (index == -1) return -1
    for (i in 0 until index) res += words[i].length
    return res + index
}

/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String {
    var res = ""
    if (description.isEmpty()) return res
    val goods = description.split("; ")
    var mPrice = 0.0
    for (good in goods) {
        val parts = good.split(" ")
        if (parts[1].toDouble() >= mPrice) {
            res = parts[0]
            mPrice = parts[1].toDouble()
        }
    }
    return res
}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (roman == "") return -1
    val ok = "IVXLCDM"
    for (ch in roman) if (ch !in ok) return -1

    var str = roman
    var res = 0

    while ("CM" in str) {
        res += 900
        str = str.substringBefore("CM") + str.substringAfter("CM")
    }
    while ("M" in str) {
        res += 1000
        str = str.substringBefore("M") + str.substringAfter("M")
    }
    while ("CD" in str) {
        res += 400
        str = str.substringBefore("CD") + str.substringAfter("CD")
    }
    while ("D" in str) {
        res += 500
        str = str.substringBefore("D") + str.substringAfter("D")
    }
    while ("XC" in str) {
        res += 90
        str = str.substringBefore("XC") + str.substringAfter("XC")
    }
    while ("C" in str) {
        res += 100
        str = str.substringBefore("C") + str.substringAfter("C")
    }
    while ("XL" in str) {
        res += 40
        str = str.substringBefore("XL") + str.substringAfter("XL")
    }
    while ("L" in str) {
        res += 50
        str = str.substringBefore("L") + str.substringAfter("L")
    }
    while ("IX" in str) {
        res += 9
        str = str.substringBefore("IX") + str.substringAfter("IX")
    }
    while ("X" in str) {
        res += 10
        str = str.substringBefore("X") + str.substringAfter("X")
    }
    while ("IV" in str) {
        res += 4
        str = str.substringBefore("IV") + str.substringAfter("IV")
    }
    while ("V" in str) {
        res += 5
        str = str.substringBefore("V") + str.substringAfter("V")
    }
    while ("I" in str) {
        res += 1
        str = str.substringBefore("I") + str.substringAfter("I")
    }

    return res
}

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    fun check(str: String): Boolean {
        val ok = " <>+-[]"
        for (ch in str) if (ch !in ok) return false
        var brackets = 0
        for (ch in str) {
            when (ch) {
                '[' -> brackets++
                ']' -> brackets--
            }
            if (brackets < 0) return false
        }
        if (brackets != 0) return false
        return true
    }

    fun findNestedLoop(cmds: String, start: Int): String {
        val field = cmds.substring(start + 1)
        val sb = StringBuilder()
        var toFind = 1
        var cur = 0
        while (toFind != 0) {
            when (field[cur]) {
                '[' -> {
                    toFind++
                    sb.append(field[cur])
                }
                ']' -> {
                    toFind--
                    if (toFind != 0) sb.append(field[cur])
                }
                else -> sb.append(field[cur])
            }
            cur++
        }
        return "$sb"
    }

    fun findReversedNestedLoop(cmds: String, end: Int): String {
        val sb = StringBuilder()
        var i = 0
        while (i != end) {
            sb.append(cmds[i])
            i++
        }
        val field = sb.toString()
        sb.clear()
        i--
        var toFind = 1
        while (toFind != 0) {
            when (field[i]) {
                ']' -> {
                    toFind++
                    sb.append(field[i])
                }
                '[' -> {
                    toFind--
                    if (toFind != 0) sb.append(field[i])
                }
                else -> sb.append(field[i])
            }
            i--
        }
        return "$sb".reversed()
    }


    var currentCell = cells / 2
    var commandsLeft = limit
    if (!check(commands)) throw IllegalArgumentException("")
    val res = mutableListOf<Int>()
    for (i in 0 until cells) res.add(0)
    fun executeCommands(cmds: String, finish: Int, res: MutableList<Int>): List<Int> {
        var currentCommand = 0
        while (currentCommand < finish && commandsLeft > 0) {
            when (cmds[currentCommand]) {
                '>' -> {
                    if (currentCell + 1 < cells) currentCell++ else throw IllegalStateException("")
                }
                '<' -> {
                    if (currentCell - 1 >= 0) currentCell-- else throw IllegalStateException("")
                }
                '+' -> res[currentCell]++
                '-' -> res[currentCell]--
                '[' -> {
                    if (res[currentCell] == 0) {
                        val nestedLoop = findNestedLoop(cmds, currentCommand)
                        currentCommand += nestedLoop.length + 1
                    }
                }
                ']' -> {
                    if (res[currentCell] != 0) {
                        val reversedNestedLoop = findReversedNestedLoop(cmds, currentCommand)
                        currentCommand -= (reversedNestedLoop.length + 1)
                    }
                }
                else -> {
                }
            }
            commandsLeft--
            currentCommand++
            //print("$commandsLeft / $limit    $res\n")
        }

        return res
    }

    executeCommands(commands, commands.length, res)

    return res
}
