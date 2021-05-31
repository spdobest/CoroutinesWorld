package asyncExampleimport kotlinx.coroutines.asyncimport kotlinx.coroutines.delayimport kotlinx.coroutines.runBlockingimport kotlin.random.Randomfun main() {    runBlocking {        val firstDeferrd = async { getFirstValue() }        val secondDeferrd = async { getSecondValue() }        println("Doing processing here")        delay(500L)        println("Waiting for values")        val firstValue = firstDeferrd.await()        val secondValue = secondDeferrd.await()        println("The total is ${firstValue+secondValue}")    }}suspend fun getFirstValue() : Int{    delay(1000L)    val value = Random.nextInt(100)    println("Returning firstValue $value")    return value}suspend fun getSecondValue() : Int{    delay(2000L)    val value = Random.nextInt(100)    println("Returning Second Value $value")    return value}