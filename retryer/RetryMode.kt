package retryer

sealed class RetryMode<T>
       class ToRetry<T>()             : RetryMode<T>()
  data class Return<T>( val value: T ): RetryMode<T>()
