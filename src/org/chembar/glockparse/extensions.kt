package org.chembar.glockparse

/**
 * 获取化学式的相对原子质量
 */
fun Formula.getRelativeMass(rmDatabase: RMDatabase) = atomMap.map { (name, num) ->
    rmDatabase.queryAtom(name) * num
}.reduce(AdvNum::plus)
