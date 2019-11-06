package nicestring

fun String.isNice(): Boolean {

    val constraint1 = setOf("bu", "ba", "be").none { this.contains(it) }

    val constraint2 = count { it in "aeiou" } >= 3

    // windowed

    var constraint3 = zipWithNext().any {
        it.first == it.second
    }

    windowed(2).any {
        it[0] == it[1]
    }

    var conditions = 0
    if (constraint1)
        conditions++
    if (constraint2)
        conditions++
    if (constraint3)
        conditions++

    return conditions >= 2
}