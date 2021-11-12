@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import lesson4.task1.convert
import java.io.File
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.max

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        if (line.isNotEmpty())
            if (line[0] == '_')
                continue
        writer.write(line)
        writer.newLine()
    }

    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val res = mutableMapOf<String, Int>()
    for (str in substrings) res[str] = 0
    for (line in File(inputName).readLines()) {
        val gLine = line.lowercase()
        for (key in res.keys) {
            val gKey = key.lowercase()
            if (gLine.length >= gKey.length) {
                var indexLeft = 0
                var indexRight = gKey.length

                while (indexRight != gLine.length + 1) {
                    if (gLine.substring(indexLeft, indexRight) == gKey)
                        res[key] = res[key]!! + 1
                    indexLeft++
                    indexRight++
                }
            }
        }
    }
    return res
}


/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    fun correct(word: String): String {
        for (ex in listOf("жури", "брошур", "парашут"))
            if (word.lowercase().substring(word.indices) in ex) return word
        val inds = mutableListOf<Int>()
        val sb = StringBuilder()
        for (sibilant in "жчшщ") {
            if (sibilant !in word.lowercase())
                continue
            for (i in word.indices)
                if (word.lowercase()[i] == sibilant && i != word.lastIndex) inds.add(i + 1)
        }
        if (inds.isEmpty()) return word
        var prev = 0
        for (index in inds.sorted()) {
            sb.append(word.substring(prev until index))
            sb.append(
                when (word[index]) {
                    'Ы' -> 'И'
                    'Я' -> 'А'
                    'Ю' -> 'У'
                    'ы' -> 'и'
                    'я' -> 'а'
                    'ю' -> 'у'
                    else -> word[index]
                }
            )
            prev = index + 1
        }
        sb.append(word.substring(prev until word.length))
        return "$sb"
    }


    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        val sb = StringBuilder()
        val words = line.split(" ")
        for (i in 0 until words.size - 1)
            sb.append(correct(words[i]) + " ")
        sb.append(correct(words.last()))
        writer.write("$sb")
        writer.newLine()
    }
    writer.close()
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val lines = File(inputName).readLines().toMutableList()
    var bigLen = 0
    for (j in lines.indices) {
        var line = lines[j]
        if (line == "") continue
        var i = 0
        while (line[i] == ' ') i++
        var k = line.length - 1
        while (line[k] == ' ') k--
        line = line.substring(i, k + 1)
        lines[j] = line
        if (line.length > bigLen) bigLen = line.length
    }
    for (line in lines) {
        val num = when (line.length) {
            bigLen -> 0
            0 -> bigLen / 2
            else -> floor((bigLen - line.length) / 2.0).toInt()
        }
        val newLine = " ".repeat(num) + line
        writer.write(newLine)
        writer.newLine()
    }
    writer.close()
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    /*
    fun String.betterIsEmpty(): Boolean =
        if (this.isEmpty()) true
        else this.all { it == ' ' }

    val input = File(inputName).readLines()
    val lines = mutableListOf<String>()
    var maxlen = 0
    for (line in input) {
        if (line.betterIsEmpty())
            lines.add("\n")
        else {
            val words = mutableListOf<String>()
            for (w in line.split(' ')) if (w.isNotEmpty()) words.add(w)
            val l = words.joinToString(separator = " ")
            maxlen = max(maxlen, l.length)
            lines.add(l)
        }
    }
*/ TODO()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val words = mutableListOf<String>()
    for (line in File(inputName).readLines()) {
        val splitLine = line.split(
            " ", ",", "!", "?", ".", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ")", "(", "—",
            "»", "*", "«", ";", ":", "<", ">", "[", "]", "{", "}", "%", "\"", "/", "+", "=", "_", "~", "\'", "|"
        )
        for (w in splitLine) if (w.isNotEmpty() && w != "\n") words.add(w.lowercase())
    }

    var cons = mutableMapOf<String, Int>()

    for (word in words)
        if (cons[word] == null)
            cons[word] = 1
        else
            cons[word] = cons[word]!! + 1
    val tmp = cons.toList().sortedByDescending { (_, v) -> v }.toMap()
    cons = tmp.toMutableMap()
    return if (tmp.keys.size <= 20)
        tmp
    else {
        val verytmp = cons.toList()[19].second
        val res = mutableMapOf<String, Int>()
        for (key in tmp.keys) if (tmp[key]!! >= verytmp) res[key] = tmp[key]!!
        res
    }
}

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val dict = mutableMapOf<Char, String>()
    for ((k, v) in dictionary)
        dict[k.lowercase()[0]] = v.lowercase()
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        val chars = mutableMapOf<Pair<Char, Int>, Boolean>()
        // Adding index to make sure every char is unique
        for (i in line.indices) chars[Pair(line[i], i)] = line[i].isUpperCase()
        val sb = StringBuilder()
        for ((ch, index) in chars.keys) {
            if (ch.lowercase()[0] in dict.keys) {
                if (chars[(Pair(ch, index))]!!) {
                    val tmp = dict[ch.lowercase()[0]]
                    if (tmp!!.isNotEmpty()) {
                        sb.append(tmp[0].uppercase())
                        if (tmp.length > 1) sb.append(tmp.substring(1).lowercase())
                    } else {
                        sb.append("")
                    }
                } else {
                    sb.append(dict[ch.lowercase()[0]]!!.lowercase())
                }
            } else {
                sb.append(ch)
            }
        }
        writer.write("$sb")
        writer.newLine()
    }
    writer.close()
}

fun checkLetters(str: String): Boolean {
    var letters = setOf<Char>()
    for (ch in str) {
        val newLetters = letters
        letters = letters + ch
        if (newLetters == letters) return false
    }
    return true
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val words = File(inputName).readLines().toMutableList()
    val res = mutableListOf<String>()
    var maxLen = 0
    for (word in words) if (checkLetters(word.lowercase())) maxLen = max(maxLen, word.length)
    for (word in words) {
        if (word.length == maxLen && checkLetters(word.lowercase())) {
            res.add(word)
            maxLen = word.length
        }
    }
    writer.write(res.joinToString(separator = ", "))
    writer.close()
}

fun String.isMarkdownEmpty(): Boolean = this.isEmpty() || this.all { it == ' ' || it == '\t' }
fun MutableList<String>.pop(): String {
    val last = this.last()
    this.removeLast()
    return last
}

/**
 * Сложная (22 балла)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val writer = File(outputName).bufferedWriter()
    val input = mutableListOf<String>()
    val sb = StringBuilder()
    val stack = mutableListOf<String>()

    sb.append("<html><body><p>")
    if (lines.isNotEmpty() && lines.any { it.isMarkdownEmpty() }) {
        var k = 0
        while (k < lines.size && lines[k].isMarkdownEmpty()) k++
        while (k < lines.size - 1) {
            if (!(lines[k].isMarkdownEmpty() && lines[k + 1].isMarkdownEmpty()))
                input.add(lines[k])
            k++
        }
        if (!(lines[k].isMarkdownEmpty())) input.add(lines[k])
        for (line in input) {
            if (line.isMarkdownEmpty()) {
                sb.append("</p><p>")
                continue
            }
            var i = 0
            while (i < line.length) {
                when (line[i]) {
                    '~' -> {
                        if (i < line.length - 1 && line[i + 1] == '~') {
                            if ("~~" in stack) {
                                stack.remove("~~")
                                sb.append("</s>")
                            } else {
                                stack.add("~~")
                                sb.append("<s>")
                            }
                            i += 2
                        }
                    }
                    '*' -> {
                        if (i < line.length - 1 && line[i + 1] == '*') {
                            if (i < line.length - 2 && line[i + 2] == '*') {
                                i += 3
                                if ("*" in stack) {
                                    if ("**" in stack) {
                                        if (stack.indexOf("**") > stack.indexOf("*"))
                                            sb.append("</b></i>")
                                        else
                                            sb.append("</i></b>")
                                        stack.remove("*")
                                        stack.remove("**")
                                    } else {
                                        stack.remove("*")
                                        stack.add("**")
                                        sb.append("</i><b>") // <--
                                    }
                                } else if ("**" in stack) {
                                    stack.remove("**")
                                    stack.add("*")
                                    sb.append("</b><i>")
                                } else {
                                    stack.add("**")
                                    stack.add("*")
                                    sb.append("<b><i>")
                                }
                            } else {
                                i += 2
                                if ("**" in stack) {
                                    stack.remove("**")
                                    sb.append("</b>")
                                } else {
                                    stack.add("**")
                                    sb.append("<b>")
                                }
                            }
                        } else {
                            i++
                            if ("*" in stack) {
                                stack.remove("*")
                                sb.append("</i>")
                            } else {
                                stack.add("*")
                                sb.append("<i>")
                            }
                        }
                    }
                    else -> {
                        sb.append(line[i])
                        i++
                    }
                }
            }
        }
    }
    sb.append("</p></body></html>")
    writer.write("$sb")
    writer.close()
}

/**
 * Сложная (23 балла)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val writer = File(outputName).bufferedWriter()
    val sb = StringBuilder()
    sb.append("<html><body><p>")
    if (lines.isNotEmpty()) {
        val digits = "0123456789."
        val stack = mutableListOf<String>()
        var k = 0
        stack.add(if (lines[0][k] == '*') "ul" else "ol")
        while (lines[0][k] in digits || lines[0][k] == ' ' || lines[0][k] == '*') k++
        sb.append("<${stack.last()}><li>${lines[0].substring(k)}")
        for (l in 1 until lines.size) {
            val line = lines[l]
            if (line.isMarkdownEmpty()) continue
            var first = 0
            while (line[first] == ' ') first++
            if (first > 4 * (stack.size - 1)) {
                stack.add(
                    when (line[first]) {
                        '*' -> {
                            sb.append("<ul>")
                            "ul"
                        }
                        in digits -> {
                            sb.append("<ol>")
                            "ol"
                        }
                        else -> throw IllegalArgumentException("Bad line format in '$line' with ${line[first]}")
                    }
                )
            } else if (first < 4 * (stack.size - 1)) {
                sb.append("</li></${stack.pop()}></li>")
            } else {
                sb.append("</li>")
            }
            while (line[first] in digits || line[first] == ' ' || line[first] == '.' || line[first] == '*') first++
            sb.append("<li>${line.substring(first)}")
        }
        while (stack.size != 0) {
            sb.append("</li></${stack.pop()}>")
        }
    }
    sb.append("</p></body></html>")
    writer.write("$sb")
    writer.close()
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val res = rhv * lhv
    val sumWidth = ("$res").length + 1
    writer.write(" ".repeat(sumWidth - "$lhv".length))
    writer.write("$lhv\n")
    writer.write("*")
    writer.write(" ".repeat(sumWidth - 1 - "$rhv".length))
    writer.write("$rhv\n")
    writer.write("-".repeat(sumWidth))
    writer.newLine()
    val rhvDigits = convert(rhv, 10)
    var i = rhvDigits.size - 1
    var curNum = lhv * rhvDigits[i]
    writer.write(" ".repeat(sumWidth - "$curNum".length))
    writer.write("$curNum\n")
    i--
    while (i != -1) {
        writer.write("+")
        curNum = lhv * rhvDigits[i]
        writer.write(" ".repeat(sumWidth + i - rhvDigits.size - "$curNum".length))
        writer.write("$curNum")
        writer.newLine()
        i--
    }
    writer.write("-".repeat(sumWidth))
    writer.newLine()
    writer.write(" ")
    writer.write("$res")
    writer.close()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val sb = StringBuilder()
    val res = lhv / rhv
    val digits = convert(lhv, 10)
    var i = 0
    var num = 0
    while (num / rhv == 0 && i < digits.size) {
        num *= 10
        num += digits[i]
        i++
    }
    var tmp = num / rhv * rhv
    val n = num.toString().length - (tmp.toString().length + 1)
    val t = "${if (n < 0) " " else ""}$lhv | $rhv\n"
    val maxlen = t.substringBefore('|').length - 1
    sb.append(t)
    sb.append(
        "${" ".repeat(max(n, 0))}-${tmp}${" ".repeat(3 + digits.size - num.toString().length)}$res\n"
    )
    var dashes = max(num.toString().length, tmp.toString().length + 1)
    sb.append("${"-".repeat(dashes)}\n")
    var rightIndent = digits.size - i
    while (i < digits.size) {
        num -= tmp
        val hasZero = if (num == 0) 1 else 0
        num *= 10
        num += digits[i++]
        rightIndent--
        tmp = num / rhv * rhv
        dashes = max(num.toString().length + hasZero, tmp.toString().length + 1)
        sb.append(
            "${" ".repeat(maxlen - num.toString().length - rightIndent - hasZero)}${if (hasZero == 1) "0" else ""}$num\n"
        )
        sb.append("${" ".repeat(maxlen - tmp.toString().length - rightIndent - 1)}-$tmp\n")
        sb.append("${" ".repeat(maxlen - dashes - rightIndent)}${"-".repeat(dashes)}\n")
    }
    sb.append("${" ".repeat(maxlen - (lhv % rhv).toString().length)}${lhv % rhv}")
    val writer = File(outputName).bufferedWriter()
    writer.write("$sb")
    writer.close()
}
