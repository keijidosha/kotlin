package retryer

interface Runner<T> {
    fun run( f: () -> T ): T
}
