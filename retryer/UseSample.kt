package retryer

import java.net.URL

object DefaultRunner: Runner<String> {
    override fun run( f: () -> String ): String {
        return f()
    }
}

val toRetry = ToRetry<String>()

interface DefaultErrorHandler<T>: Retryer<String> {
    override fun onErrorRetry( retryedCount: Int, ex: Throwable ): RetryMode<String> {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount)" + ex.message )
        return toRetry
    }
    override fun onRetryOver( retryedCount: Int, ex: Throwable ): String {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount:final)" + ex.message )
        throw ex
    }
}

interface RecoveryErrorHandler<T>: Retryer<String> {
    override fun onErrorRetry( retryedCount: Int, ex: Throwable ): RetryMode<String> {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount)" + ex.message )
        return Return( "Oooooo..." )
    }
    override fun onRetryOver( retryedCount: Int, ex: Throwable ): String {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount:final)" + ex.message )
        throw ex
        //return "Retry Over"
    }
}

interface RecoveryAtLastErrorHandler<T>: Retryer<String> {
    override fun onErrorRetry( retryedCount: Int, ex: Throwable ): RetryMode<String> {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount)" + ex.message )
        return toRetry
    }
    override fun onRetryOver( retryedCount: Int, ex: Throwable ): String {
        println( java.text.SimpleDateFormat( "HH:mm:ss.SSSS" ).format( java.util.Date()) + ": ($retryedCount:final)" + ex.message )
        return "Retry Over"
    }
}

object Config {
    val initialInterval = 500L
    val maxInterval = 5000L
    val maxRetry = 5
}

object ConstantRetryer_1: ConstantIntervalRetryer<String>, DefaultErrorHandler<String> {
    override val initialInterval = Config.initialInterval
    override val maxInterval = Config.maxInterval
    override val maxRetry = Config.maxRetry
}

object IncrementalRetryer_1: IncrementalIntervalRetryer<String>, RecoveryErrorHandler<String> {
    override val initialInterval = Config.initialInterval
    override val maxInterval = Config.maxInterval
    override val maxRetry = Config.maxRetry
}

object DoublingRetryer_1: DoublingIntervalRetryer<String>, RecoveryAtLastErrorHandler<String> {
    override val initialInterval = Config.initialInterval
    override val maxInterval = Config.maxInterval
    override val maxRetry = Config.maxRetry
}

fun main( args: Array<String> ) {
    println(
        DefaultRunner.run() {
            URL( "https://raw.githubusercontent.com/keijidosha/kotlin/master/retryer/Runner.kt" ).readText()
        }
    )

    println( "------------------------------" )

    try {
        println(
            ConstantRetryer_1.run() {
                URL( "http://localhost:54321/" ).readText()
            }
        )
    } catch( ex: Throwable ) {
        println( "Exception at retryer: ${ex.message}" )
    }

    println( "------------------------------" )

    @Suppress("UNCHECKED_CAST")
    val runner1 = Class.forName( "retryer.DefaultRunner" ).kotlin.objectInstance as Runner<String>
    try {
        println( runner1.run() {
            URL( "http://localhost:54321/" ).readText()
        } )
    } catch( ex: Exception ) { println( ex.toString()) }

    println( "------------------------------" )

    @Suppress("UNCHECKED_CAST")
    val constantRetryer1 = Class.forName( "retryer.ConstantRetryer_1" ).kotlin.objectInstance as Runner<String>
    try {
        println( constantRetryer1.run() { URL( "http://localhost:54321/" ).readText() } )
    } catch( ex: Exception ) { println( ex.toString()) }

    println( "------------------------------" )

    @Suppress("UNCHECKED_CAST")
    val constantRetryer2 = Class.forName( "retryer.IncrementalRetryer_1" ).kotlin.objectInstance as Runner<String>
    try {
        println( constantRetryer2.run() { URL( "http://localhost:54321/" ).readText() } )
    } catch( ex: Exception ) { println( ex.toString()) }

    println( "------------------------------" )

    @Suppress("UNCHECKED_CAST")
    val constantRetryer3 = Class.forName( "retryer.DoublingRetryer_1" ).kotlin.objectInstance as Runner<String>
    try {
        println( constantRetryer3.run() { URL( "http://localhost:54321/" ).readText() } )
    } catch( ex: Exception ) { println( ex.toString()) }

}