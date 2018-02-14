package org.chembar.glockparse

/**
 * Formula -- 化学式抽象
 * @author DuckSoft
 * @version 0.2
 */
class Formula
/**
 * 构造函数。
 * 以给定的[formulaText]构造一个化学式对象。
 * @param formulaText 化学式字符串。
 * 可接受的化学式字符串格式如下：
 *  - CuSO4.5H2O
 *  - Fe3(Fe(CN)6)2
 *  - 2Cu(OH)2.16KOH.4H2O
 * @return 化学方程式对象
 */
(val formulaText: String) {
    override fun toString() = formulaText

    /**
     * 原子名称-个数映射表
     */
    val atomMap = formulaText.split('.').map {
        fun defaultOne(str: String) = if (str == "") 1 else str.toInt()

        fun parse(f: String, m: Int = 1): HashMap<String, Int> {
            val atomMap = HashMap<String, Int>()
            Regex("(\\((.+)\\)|([A-Z][a-z]*))(\\d*)").findAll(f).asSequence().apply {
                if (this.map { it.groupValues[0].length }.sum() != f.length) {
                    throw FormulaParsingException("化学式格式错误")
                }
            }.forEach {
                it.groupValues.apply {
                    if (this[2] != "") {
                        atomMap.merge(parse(this[2], m*defaultOne(this[4])))
                    } else {
                        atomMap.merge(this[3], m*defaultOne(this[4]), Int::plus)
                    }
                }
            }
            return atomMap
        }
        Regex("(\\d+)?(.+)").matchEntire(it).let {
            it?.groupValues ?: throw FormulaParsingException("段分析出错")
        }.map { it.replace(" ", "") }.let {
            parse(it[2], it[1].let{ defaultOne(it) })
        }
    }.reduceRight { hashMap, acc ->
        acc.apply {
            this.merge(hashMap)
        }
    }

    /**
     * FormulaParsingException -- 化学式分析异常类
     */
    class FormulaParsingException(reason: String) : Exception("Formula parsing error: $reason")
}

