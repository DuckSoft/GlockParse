package org.chembar.glockparse

/**
 * AdvNum -- 带最坏误差估计的双精度类型
 * @author DuckSoft
 * @version 0.2
 */
class AdvNum {
    private var numInner: Double = 0.0
    private var numMax: Double = 0.0
    private var numMin: Double = 0.0

    constructor(number: Double) {
        number.let {
            numInner = it
            numMin = it
            numMax = it
        }
    }

    constructor(numCenter: Double, numError: Double) {
        assert(numError > 0)

        // FIXME: must be positive!
        assert(numCenter - numError > 0)


        numCenter.let {
            numInner = it
            numMin = it - numError
            numMax = it + numError
        }
    }

    constructor(numCenter: Double,  numLower: Double, numUpper: Double) {
        assert(numCenter in numLower..numUpper)

        this.numInner = numCenter
        this.numMin = numLower
        this.numMax = numUpper
    }

    // 二元运算符重载
    operator fun plus(op:AdvNum): AdvNum {
        return AdvNum(
                numInner + op.numInner,
                numMin + op.numMin,
                numMax + op.numMax
        )
    }
    operator fun minus(op:AdvNum): AdvNum {
        return AdvNum(
                numInner - op.numInner,
                numMin - op.numMin,
                numMax - op.numMax
        )
    }
    operator fun times(op:AdvNum): AdvNum {
        // FIXME: must be positive!
        return AdvNum(
                numInner * op.numInner,
                numMin * op.numMin,
                numMax * op.numMax
        )
    }
    operator fun div(op:AdvNum): AdvNum {
        // FIXME: must be positive!
        return AdvNum(
                numInner / op.numInner,
                numMin / op.numMax,
                numMax / op.numMin
        )
    }

    // Kotlin自动解构用
    operator fun component1() = numInner
    operator fun component2() = numMin
    operator fun component3() = numMax

    /**
     * 获取本[AdvNum]的字符串表示。
     */
    override fun toString(): String {
        return "<AdvNum:$numInner<-($numMin,$numMax)>"
    }
}