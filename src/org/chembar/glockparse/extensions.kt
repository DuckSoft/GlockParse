package org.chembar.glockparse

/**
 * 获取化学式的相对原子质量
 */
fun Formula.getRelativeMass(rmDatabase: RMDatabase) = atomMap.map { (name, num) ->
    rmDatabase.queryAtom(name) * num
}.reduce(AdvNum::plus)

fun HashMap<String, Int>.merge(sourceMap: HashMap<String, Int>) {
    sourceMap.forEach { atom, num -> this.merge(atom, num, Int::plus) }
}

fun Equation.checkBalance() = this.sides.map {
    it.map { (formula, _) -> formula.atomMap }.reduceRight { hashMap, acc ->
        acc.apply { acc.merge(hashMap) }
    }
}.let {
    it[0].forEach { (t, u) -> if (it[1][t] != u) return@let false }
    true
}
