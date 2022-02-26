package promax.dohaumen.financeapp.helper

import java.util.*


fun <K, V> Map<K, V>.getKey(value: V): K? {
    for ((key, value1) in this) {
        if (Objects.equals(value, value1)) {
            return key
        }
    }
    return null
}

fun <T> Collection<T>.copyOf(): Collection<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}

fun <T> Collection<T>.mutableCopyOf(): MutableCollection<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}
