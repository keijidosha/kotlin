package retryer

interface Runner<T> {
    fun run( f: () -> T ): T
}

object DefaultRunner: Runner<String> {
    override fun run( f: () -> String ): String {
        return f()
    }
}
