package channelsimport kotlinx.coroutines.CoroutineScopeimport kotlinx.coroutines.channels.ReceiveChannelimport kotlinx.coroutines.channels.produceimport kotlinx.coroutines.delayimport kotlinx.coroutines.launchimport kotlinx.coroutines.runBlockingfun main() {    runBlocking {        val processor = produceNumbers1()        repeat(5) { launchProcessor(it, processor) }        delay(1500)        processor.cancel()    }}fun CoroutineScope.produceNumbers1() =  produce<Int>{    var x = 1    while(true){        send(x++)        delay(100L)    }}fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {    for(msg in channel){        println("processor #$id received $msg")    }}