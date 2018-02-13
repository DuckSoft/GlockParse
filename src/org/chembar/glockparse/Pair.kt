package org.chembar.glockparse

/** Pair
 * @author DuckSoft
 *
 * @param L Pair左侧类型
 * @param R Pair右侧类型
 */
class Pair<L, R>(var l: L, var r: R) {
    // 用于Kotlin自动解构
    operator fun component1() = l
    operator fun component2() = r
    // 用于调试输出
    override fun toString() = "($l,$r)"
}
