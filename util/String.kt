package util

import kotlin.text.*

class StringExtension

fun Regex.match( str: String ): Collection<MatchGroup?> = find( str )?.groups ?: listOf<MatchGroup>()