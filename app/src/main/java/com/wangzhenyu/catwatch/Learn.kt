package com.wangzhenyu.catwatch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun main() {
    runBlocking {
        launch {

            val f = flow {
                for (i in 1..5) {
                    delay(500)
                    println("emit $i")
                    emit(i)
                }
            }

            withTimeoutOrNull(3600)
            {

                f.collect {
                    delay(500)
                    println("collect $it")
                }

            }


            println("ha ha ha")

        }
    }


}