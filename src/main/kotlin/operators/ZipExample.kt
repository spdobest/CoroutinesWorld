package operatorsimport kotlinx.coroutines.flow.collectimport kotlinx.coroutines.flow.flowOfimport kotlinx.coroutines.flow.zipimport kotlinx.coroutines.runBlockingfun main() {    runBlocking {        zipExample()    }}suspend fun zipExample(){    val english = flowOf("One", "Two", "Three")    val hindi = flowOf("ek", "do", "teen")    english.zip(hindi) { a,b -> "'$a in Hindi is $b'"}        .collect {            println(it)        }}