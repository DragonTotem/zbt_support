package com.zbt.sample

import kotlin.properties.Delegates

/**
 * Author       :zbt
 * Date         :2020/10/28 下午5:16
 * Version      :1.0.0
 * Description  :
 */


typealias SumAlias = (Int, Int) -> Int


fun printSum(a: Int, b: Int, block: SumAlias) {
    println(block.invoke(a, b))
}

class Worker {
    var name: String by Delegates.notNull()

    var id: String by Delegates.observable(
        initialValue = "123",
        onChange = { property, oldValue, newValue ->
            println("property:${property.name} oldValue: $oldValue  newValue:$newValue")
        })

    var work: String by Delegates.vetoable(
        initialValue = "java",
        onChange = { property, oldValue, newValue ->
            println("property: ${property.name}  oldValue: $oldValue  newValue: $newValue")
            return@vetoable newValue == "kotlin"
        })
}

fun main(args: Array<String>) {
    val worker = Worker().apply {
        name = "zbt"
    }
    println(worker.name)

    val worker2 = Worker().apply {
        id = "456"
    }
    worker2.id = "789"
    worker2.id = "890"
    worker2.id = "921"

    val worker3 = Worker().apply { work = "java" }
    worker3.work = "java"
    worker3.work = "android"
    worker3.work = "kotlin"
    println("work is ${worker3.work}")
}


fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun test() {

}