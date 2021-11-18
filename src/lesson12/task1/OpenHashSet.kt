@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

/**
 * Класс "хеш-таблица с открытой адресацией"
 *
 * Общая сложность задания -- сложная, общая ценность в баллах -- 20.
 * Объект класса хранит данные типа T в виде хеш-таблицы.
 * Хеш-таблица не может содержать равные по equals элементы.
 * Подробности по организации см. статью википедии "Хеш-таблица", раздел "Открытая адресация".
 * Методы: добавление элемента, проверка вхождения элемента, сравнение двух таблиц на равенство.
 * В этом задании не разрешается использовать библиотечные классы HashSet, HashMap и им подобные,
 * а также любые функции, создающие множества (mutableSetOf и пр.).
 *
 * В конструктор хеш-таблицы передаётся её вместимость (максимальное количество элементов)
 */
class OpenHashSet<T>(val capacity: Int) {

    /**
     * Массив для хранения элементов хеш-таблицы
     */
    internal val elements = Array<Any?>(capacity) { null }

    /**
     * Число элементов в хеш-таблице
     */
    val size: Int
        get() {
            var res = 0
            for (el in elements)
                when (el) {
                    null -> continue
                    is MutableList<*> -> res += el.size
                    else -> res++
                }
            return res
        }

    /**
     * Признак пустоты
     */
    fun isEmpty(): Boolean = this.size == 0

    /**
     * Добавление элемента.
     * Вернуть true, если элемент был успешно добавлен,
     * или false, если такой элемент уже был в таблице, или превышена вместимость таблицы.
     */
    fun add(element: T): Boolean {
        if (element in this || this.size == capacity) return false
        val i = element.hashCode() % capacity
        when (val suspect = elements[i]) {
            null -> elements[i] = element
            is MutableList<*> -> elements[i] = mutableListOf(element) + suspect
            else -> elements[i] = mutableListOf(suspect, element)
        }
        return true
    }

    /**
     * Проверка, входит ли заданный элемент в хеш-таблицу
     */
    operator fun contains(element: T): Boolean {
        val i = element.hashCode() % capacity
        when (val suspect = elements[i]) {
            null -> return false
            is MutableList<*> -> element in suspect
            else -> return suspect == element
        }
        return true
    }

    /**
     * Таблицы равны, если в них одинаковое количество элементов,
     * и любой элемент из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (other is OpenHashSet<*> && other.size == this.size) {
            for (el in other.elements)
                when (el) {
                    null -> continue
                    is MutableList<*> -> if (el.any { it as T !in this }) return false
                    else -> if (el as T !in this) return false
                }
            return true
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        var res = 0
        for (el in elements) if (el != null) res += el.hashCode()
        return res
    }
}