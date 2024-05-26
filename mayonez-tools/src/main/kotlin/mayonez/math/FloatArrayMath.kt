package mayonez.math

fun FloatArray.plusArray(other: FloatArray): FloatArray {
    return FloatArray(this.size) { this[it] + other[it] }
}

fun FloatArray.minusArray(other: FloatArray): FloatArray {
    return FloatArray(this.size) { this[it] - other[it] }
}

fun FloatArray.timesFloat(num: Float): FloatArray {
    return FloatArray(this.size) { this[it] * num }
}

fun FloatArray.equalsArray(other: FloatArray): Boolean {
    for (i in this.indices) {
        if (!MathUtils.equals(this[i], other[i])) {
            return false
        }
    }
    return true
}