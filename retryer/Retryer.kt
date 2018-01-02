package retryer

interface Retryer<T>: Runner<T> {
    val initialInterval: Long
    val maxInterval:     Long
    val maxRetry:        Int
    fun nextInterval( lastInterval: Long ): Long
    fun onErrorRetry( retryedCount: Int, ex: Throwable ): RetryMode<T>
    fun onRetryOver(  retryedCount: Int, ex: Throwable ): T
}

interface RetryerFoundation<T>: Retryer<T> {
    override fun run( f: () -> T ): T {
        var cnt = 1
        var interval = initialInterval
        while( true ) {
            try {
                return f()
            }
            catch( ex: Throwable ) {
                if( cnt == maxRetry ) {
                    return onRetryOver( cnt, ex )
                }
                else {
                    val ret = onErrorRetry( cnt, ex )
                    when( ret ) {
                        is ToRetry -> {}
                        is Return<T> -> return ret.value
                    }
                }
            }
            try {
                Thread.sleep( interval )
            } catch( ex: InterruptedException ) { println( ex.toString()) }
            cnt += 1
            interval = nextInterval( interval )
        }
    }
}

interface ConstantIntervalRetryer<T>: RetryerFoundation<T> {
    override fun nextInterval( lastInterval: Long ) = initialInterval
}

interface IncrementalIntervalRetryer<T>: RetryerFoundation<T> {
    override fun nextInterval( lastInterval: Long ): Long {
        val interval = lastInterval + initialInterval
        return if( interval > maxInterval ) maxInterval else interval
    }
}

interface DoublingIntervalRetryer<T>: RetryerFoundation<T> {
    override fun nextInterval( lastInterval: Long ): Long {
        val interval = lastInterval shl 1
        return if( interval > maxInterval ) maxInterval else interval
    }
}
