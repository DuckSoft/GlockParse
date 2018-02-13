package org.chembar.glockparse

/**
 * Equation -- 化学方程式抽象
 * @author DuckSoft
 * @version 0.2
 */
class Equation
/**
 * 构造函数。
 * 以给定的[equationText]构造一个化学方程式对象。
 * @param equationText 输入的化学方程式对象
 * 可接受的格式示例如下：
 *  - 2 C + O2 = 2 CO
 *  - C + O2 - CO2
 *  - C + O2 --> CO2
 *  - C + O2 ==> CO2
 *  - ...
 * @return 化学方程式对象
 * @throws EquationParseError
 */
@Throws(EquationParseError::class) constructor
(val equationText: String) {
    val sides = equationText.split(Regex("(-+|=+)>?"), 2).apply {
        // 将化学方程式左右两侧分开
        // 若所得结果数不为2，则抛出异常
        if (this.size != 2) throw EquationParseError("无法分离两侧方程式")
    }.map {
        it.split("+").map(String::trim).map {
            // 为map准备变量
            lateinit var formula: Formula
            var num = 1

            // 若化学式中包含空格，则视作有化学计量数
            // 否则一律当作化学式内部处理
            if (it.contains(' ')) Regex("(\\d+) +(.+)").matchEntire(it).let {
                it?.groupValues?:throw EquationParseError("无法分析化学式：$it")
            }.apply {
                if (this.size != 3) throw EquationParseError("分析化学式时出现问题")
            }.let {
                formula = Formula(it[2])
                num = it[1].toInt()
            } else formula = Formula(it)

            // 为map返回Pair
            Pair(formula, num)
        }
    }

    /** 获取本化学方程式对象的字符串表示 */
    override fun toString() = sides.toString()

    // 用于Kotlin自动解构
    operator fun component1() = sides[0]
    operator fun component2() = sides[1]

    /**
     * EquationParseError -- 方程式分析异常类
     * @see [Equation]
     */
    inner class EquationParseError(reason: String) : Exception("Equation Parsing Error: $reason")
}