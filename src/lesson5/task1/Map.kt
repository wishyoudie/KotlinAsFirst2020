@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import java.lang.NullPointerException
import java.util.*

// Урок 5: ассоциативные массивы и множества
// Максимальное количество баллов = 14
// Рекомендуемое количество баллов = 9
// Вместе с предыдущими уроками = 33/47
class MyGraph {
    private data class Vertex(val name: String) {
        val heirs = mutableSetOf<Vertex>()
    }

    private val vertices = mutableMapOf<String, Vertex>()

    private operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    private fun cmp(th: MyGraph, ot: MyGraph): Boolean {
        for ((name, vert) in ot.vertices) {
            try {
                if (th.vertices[name]!!.heirs != vert.heirs) {
                    println("F")
                    return false
                }
            } catch (e: NullPointerException) {
                return false
            }
        }
        return true
    }

    override fun equals(other: Any?) =
        other is MyGraph && cmp(this, other)


    fun addVertex(name: String) {
        vertices[name] = Vertex(name)
    }

    private fun connect(first: Vertex, second: Vertex) {
        first.heirs.add(second)
        for (i in second.heirs)
            first.heirs.add(i)
    }

    fun connect(first: String, second: String) {
        if (first != second) connect(this[first], this[second])
    }

    fun toMap(): Map<String, Set<String>> {
        val res = mutableMapOf<String, Set<String>>()
        for ((name, vert) in vertices) {
            val setOfHeirs = mutableSetOf<String>()
            for (heir in vert.heirs) {
                if (heir.name != name)
                    setOfHeirs.add(heir.name)
            }
            res[name] = setOfHeirs
        }
        return res
    }

    /**
     * Пример
     *
     * По двум вершинам рассчитать расстояние между ними = число дуг на самом коротком пути между ними.
     * Вернуть -1, если пути между вершинами не существует.
     *
     * Используется поиск в ширину
     */
    fun bfs(start: String, finish: String) = bfs(this[start], this[finish])

    private fun bfs(start: Vertex, finish: Vertex): Int {
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        val inheritors = mutableMapOf(start to 0)
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val distance = inheritors[next]!!
            if (next == finish) return distance
            for (heir in next.heirs) {
                if (heir in inheritors) continue
                inheritors[heir] = distance + 1
                queue.add(heir)
            }
        }
        return -1
    }

    /**
     * Пример
     *
     * По двум вершинам рассчитать расстояние между ними = число дуг на самом коротком пути между ними.
     * Вернуть -1, если пути между вершинами не существует.
     *
     * Используется поиск в глубину
     */
    fun dfs(start: String, finish: String): Int = dfs(this[start], this[finish], setOf()) ?: -1

    private fun dfs(start: Vertex, finish: Vertex, visited: Set<Vertex>): Int? =
        if (start == finish) 0
        else {
            val min = start.heirs
                .filter { it !in visited }
                .mapNotNull { dfs(it, finish, visited + start) }
                .minOrNull()
            if (min == null) null else min + 1
        }
}


/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
    shoppingList: List<String>,
    costs: Map<String, Double>
): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
    phoneBook: MutableMap<String, String>,
    countryCode: String
) {
    val namesToRemove = mutableListOf<String>()

    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
    text: List<String>,
    vararg fillerWords: String
): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}


/**
 * Простая (2 балла)
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */
fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    var res = mapOf<Int, List<String>>()

    for ((student, grade) in grades) {
        if (grade in res) {
            var tmp = res[grade]
            tmp = tmp!! + student
            res = res + Pair(grade, tmp)
        } else
            res = res + Pair(grade, listOf(student))
    }
    return res
}

/**
 * Простая (2 балла)
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean {
    for (key in a.keys) {
        if (!b.containsKey(key)) return false
        if (a[key] != b[key]) return false
    }
    return true
}

/**
 * Простая (2 балла)
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>) {
    for ((key, value) in b) if (a.containsKey(key) && a[key] == value) a.remove(key)
}

/**
 * Простая (2 балла)
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках.
 * В выходном списке не должно быть повторяющихся элементов,
 * т. е. whoAreInBoth(listOf("Марат", "Семён, "Марат"), listOf("Марат", "Марат")) == listOf("Марат")
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> {
    var res = setOf<String>()
    for (person in a) if (person in b) res = res + person
    return res.toList()
}

/**
 * Средняя (3 балла)
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {
    var res = mapA
    for ((name, phone) in mapB) {
        if (res.containsKey(name) && res[name] != phone) {
            var tmp = res[name]
            tmp = tmp!! + ", $phone"
            res = res + Pair(name, tmp)
        } else {
            res = res + Pair(name, phone)
        }
    }
    return res
}

/**
 * Средняя (4 балла)
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val goods = mutableMapOf<String, Pair<Int, Double>>()
    val res = mutableMapOf<String, Double>()

    for ((name, price) in stockPrices) {
        if (goods[name] != null) {
            goods[name] = Pair(goods[name]!!.first + 1, goods[name]!!.second + price)
        } else {
            goods[name] = Pair(1, price)
        }
    }
    for (i in goods.keys) {
        res[i] = goods[i]!!.second / goods[i]!!.first
    }
    return res
}

/**
 * Средняя (4 балла)
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */
fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? {
    if (stuff.isEmpty()) return null
    var res = ""
    var flag = false
    var minPrice = Double.MAX_VALUE
    for ((name, p) in stuff) {
        if (p.first == kind && p.second <= minPrice) {
            minPrice = p.second
            res = name
            flag = true
        }
    }
    return if (flag) res else null
}

/**
 * Средняя (3 балла)
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */
fun canBuildFrom(chars: List<Char>, word: String): Boolean {
    if (word == "") return true

    val letters = mutableSetOf<Char>()
    for (ch in word) letters.add(ch.lowercaseChar())

    val lcChars = mutableSetOf<Char>()
    for (ch in chars) lcChars.add(ch.lowercaseChar())

    for (el in letters) if (el !in lcChars) return false
    return true
}

/**
 * Средняя (4 балла)
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */
fun extractRepeats(list: List<String>): Map<String, Int> {
    val letters = mutableSetOf<String>()
    var res = mapOf<String, Int>()
    for (ch in list) {
        if (ch in letters)
            if (ch in res) {
                var tmp = res[ch]
                tmp = tmp!! + 1
                res = res + Pair(ch, tmp)
            } else {
                res = res + Pair(ch, 2)
            }
        letters.add(ch)
    }
    return res
}

/**
 * Средняя (3 балла)
 *
 * Для заданного списка слов определить, содержит ли он анаграммы.
 * Два слова здесь считаются анаграммами, если они имеют одинаковую длину
 * и одно можно составить из второго перестановкой его букв.
 * Скажем, тор и рот или роза и азор это анаграммы,
 * а поле и полено -- нет.
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */
fun hasAnagrams(words: List<String>): Boolean {
    // Я пробовал через мапы и множества: получалось гораздо сложнее
    if (words.isEmpty()) return false
    val newWords = mutableListOf<List<Char>>()
    for (word in words) {
        val tmpList = mutableListOf<Char>()
        for (ch in word) tmpList.add(ch)
        tmpList.sort()
        if (tmpList in newWords) return true
        newWords.add(tmpList)
    }
    return false
}

/*
    fun go(person: String, visited: Set<String>): MutableSet<String> {
        val newSet = mutableSetOf<String>()
        if (friends[person] == null)
            return newSet
        newSet += friends[person]!! - visited
        for (newPerson in newSet)
            newSet += go(newPerson, visited + newPerson)
        return newSet
    }
    // [Marat, Mikhail, Sveta, Friend, GoodGnome, EvilGnome]
    for (person in people) {
        val resSet = go(person, setOf())
        res[person] = resSet
    }
    return res */

/**
 * Сложная (5 баллов)
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 *
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Оставлять пустой список знакомых для людей, которые их не имеют (см. EvilGnome ниже),
 * в том числе для случая, когда данного человека нет в ключах, но он есть в значениях
 * (см. GoodGnome ниже).
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta"),
 *       "Friend" to setOf("GoodGnome"),
 *       "EvilGnome" to setOf()
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat"),
 *          "Friend" to setOf("GoodGnome"),
 *          "EvilGnome" to setOf(),
 *          "GoodGnome" to setOf()
 *        )
 */
fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val g = MyGraph()
    val people = mutableSetOf<String>()
    // Create full people list
    for (person in friends.keys) {
        people.add(person)
        for (anotherPerson in friends[person]!!)
            people.add(anotherPerson)
    }
    // Create vertices
    /*for (person in people) g.addVertex(person)
    // Create connections (how many times?)
    while () {
        for (person in people) {
            if (friends[person] != null)
                for (anotherPerson in friends[person]!!)
                    g.connect(person, anotherPerson)
        }
    }
    return g.toMap()*/
    TODO()
}

/**
 * Сложная (6 баллов)
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    if (list.isEmpty() || list.size == 1) return Pair(-1, -1)
    when {
        number > 0 -> {
            val ind = mutableListOf<Int>()
            for (i in 0..number) ind.add(-1)
            for (i in list.indices) {
                val x = list[i]
                if (x > number) continue
                if (ind[x] != -1) return Pair(ind[x], i)
                if (ind[number - x] == -1) ind[number - x] = i
            }
        }
        number < 0 -> return Pair(-1, -1)
        number == 0 -> {
            val ind = mutableListOf<Int>()
            for (i in list.indices) if (list[i] == 0) ind.add(i)
            if (ind.size >= 2) return Pair(ind[0], ind[1])
        }
    }
    return Pair(-1, -1)
}

/**
 * Очень сложная (8 баллов)
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Перед решением этой задачи лучше прочитать статью Википедии "Динамическое программирование".
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */
fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> = TODO()


fun main() {
    println(
        propagateHandshakes(
            mapOf(
                "Marat" to setOf("Mikhail", "Sveta"),
                "Sveta" to setOf("Marat"),
                "Mikhail" to setOf("Sveta"),
                "Friend" to setOf("GoodGnome"),
                "EvilGnome" to setOf()
            )
        )
    )
}