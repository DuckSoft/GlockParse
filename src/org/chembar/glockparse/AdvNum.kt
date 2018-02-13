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

    // 与双精度浮点数的二元运算符重载
    operator fun plus(op: Double) = plus(AdvNum(op))
    operator fun minus(op: Double) = minus(AdvNum(op))
    operator fun times(op: Double) = times(AdvNum(op))
    operator fun div(op: Double) = div(AdvNum(op))

    // 与整数的二元运算符重载
    operator fun plus(op: Int) = plus(op.toDouble())
    operator fun minus(op: Int) = minus(op.toDouble())
    operator fun times(op: Int) = times(op.toDouble())
    operator fun div(op: Int) = div(op.toDouble())

    // 自运算符重载
    operator fun plusAssign(op: AdvNum) {
        this.numInner += op.numInner
        this.numMin += op.numMin
        this.numMax += op.numMax
    }
    operator fun minusAssign(op: AdvNum) {
        this.numInner -= op.numInner
        this.numMin -= op.numMin
        this.numMax -= op.numMax
    }
    operator fun timesAssign(op: AdvNum) {
        // FIXME: must be positive!
        this.numInner *= op.numInner
        this.numMin *= op.numMin
        this.numMax *= op.numMax
    }
    operator fun divAssign(op: AdvNum) {
        // FIXME: must be positive!
        this.numInner /= op.numInner
        this.numMin /= op.numMax
        this.numMax /= op.numMin
    }

    // Kotlin自动解构用
    operator fun component1() = numInner
    operator fun component2() = numMin
    operator fun component3() = numMax


    /**
     * 获取中心数。
     * 亦即最大数与最小数的**平均值**。
     */
    val central get() = (numMax + numMin) / 2

    /**
     * 使本[AdvNum]中心化。
     * 亦即令实际数值等于中心数（[central]）。
     *
     */
    fun centralize() {numInner = central}

    // 误差系列
    val errUpper get() = numMax - numInner
    val errLower get() = numInner - numMin
    val errTotal get() = numMax - numMin

    /**
     * 获取本[AdvNum]的字符串表示。
     */
    override fun toString(): String {
        return "<AdvNum:$numInner<-($numMin,$numMax)>"
    }
}