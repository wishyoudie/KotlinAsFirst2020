@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import lesson4.task1.CharMulInt
import lesson4.task1.convert
import ru.spbstu.wheels.NullableMonad.filter
import java.io.File
import kotlin.math.floor
import kotlin.math.max
import java.util.ArrayDeque

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
    TODO()
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
    val tmp = cons.toList().sortedByDescending { (k, v) -> v }.toMap()
    cons = tmp.toMutableMap()
    //cons.forEach { (k, v) -> println("$k => $v") }
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

/**
 * Сложная (22 балла)
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val writer = File(outputName).bufferedWriter()
    if (lines.isEmpty())
        writer.write("<html><body><p></p></body></html>")
    else {
        val sb = StringBuilder()
        val stack = mutableListOf<String>()
        sb.append("<html><body><p>")
        for (line in lines) {
            if (line.isEmpty() || line == " " || line == "\t") {
                if ("$sb".length > 7 && sb.toString().substring(sb.length - 7) == "</p><p>")
                    continue
                sb.append("</p>")
                if (lines.indexOf(line) != lines.size - 1)
                    sb.append("<p>")
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
        sb.append("</p></body></html>")
        writer.write(sb.toString().replace("<p></p>", ""))
    }
    writer.close()
}

fun countIndent(str: String): Int {
    var res = 0
    while (str[res++] == ' ') {
    }
    return res - 1
}

/**
 * Сложная (23 балла)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
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
    writer.write(CharMulInt(' ', sumWidth - "$lhv".length))
    writer.write("$lhv\n")
    writer.write("*")
    writer.write(CharMulInt(' ', sumWidth - 1 - "$rhv".length))
    writer.write("$rhv\n")
    writer.write(CharMulInt('-', sumWidth))
    writer.newLine()
    val rhvdigits = convert(rhv, 10)
    var i = rhvdigits.size - 1
    var curNum = lhv * rhvdigits[i]
    writer.write(CharMulInt(' ', sumWidth - "$curNum".length))
    writer.write("$curNum\n")
    i--
    while (i != -1) {
        writer.write("+")
        curNum = lhv * rhvdigits[i]
        writer.write(CharMulInt(' ', sumWidth + i - rhvdigits.size - "$curNum".length))
        writer.write("$curNum")
        writer.newLine()
        i--
    }
    writer.write(CharMulInt('-', sumWidth))
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
    TODO()
}

/*
val lines = File(inputName).readLines()
    val paragraphs = splitToParagraphs(lines)
    val sb = StringBuilder()
    sb.append("<html><body>")
    for (paragraph in paragraphs) {
        if (paragraph.isEmpty()) continue
        sb.append("<p>")
        for (line in paragraph) {
            var flagItalic = false
            var flagBold = false
            var flagCrossed = false
            var flagBoth = false
            var i = 0
            while (i < line.length && line[i] != '\n') {
                if (line[i] == '~' && line[i + 1] == '~') {
                    sb.append(if (flagCrossed) "</s>" else "<s>")
                    flagCrossed = !flagCrossed
                    i += 2
                } else if (line[i] == '*') {
                    if (i < line.length - 1 && line[i + 1] == '*') {
                        if (i < line.length - 2 && line[i + 2] == '*') {
                            if (!flagBold) {
                                if (!flagItalic) {
                                    sb.append("<b><i>")
                                    flagBoth = true
                                } else
                                    sb.append("</i><b>")
                            } else {
                                if (!flagItalic)
                                    sb.append("</b><i>")
                                else {
                                    if (flagBoth) {
                                        sb.append("</i></b>")
                                        flagBoth = false
                                    } else
                                        sb.append("</b></i>")
                                }
                            }
                            flagItalic = !flagItalic
                            flagBold = !flagBold
                            i += 3
                        } else {
                            sb.append(if (flagBold) "</b>" else "<b>")
                            flagBold = !flagBold
                            i += 2
                        }
                    } else {
                        sb.append(if (flagItalic) "</i>" else "<i>")
                        flagItalic = !flagItalic
                        i++
                    }
                } else {
                    sb.append(line[i])
                    i++
                }
            }
            sb.append("\n")
        }
        sb.append("</p>")
    }
    sb.append("</body></html>")
    val writer = File(outputName).bufferedWriter()
    writer.write("$sb")
    writer.close()
 */