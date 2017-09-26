class ByteArrayExtension {
    companion object {
        fun fromIntToBytesWithLittleEndian( intValue: Int, length: Int ): ByteArray {
            val buf = ByteArray( length )
            var remain = intValue

            repeat( length ) { idx ->
                val value = remain % 256
                buf[idx] = value.toByte()
                remain = remain shr 8
            }

            return buf
        }

        fun fromLongToBytesWithLittleEndian( longValue: Long, length: Int ): ByteArray {
            val buf = ByteArray( length )
            var remain = longValue

            repeat( length ) { idx ->
                val value = remain % 256
                buf[idx] = value.toByte()
                remain = remain shr 8
            }

            return buf
        }

        fun fromIntToBytesWithBigEndian( intValue: Int, length: Int ): ByteArray {
            val buf = ByteArray( length )
            var remain = intValue
            val maxIdx = length - 1

            repeat( length ) { idx ->
                val value = remain % 256
                buf[maxIdx - idx] = value.toByte()
                remain = remain shr 8
            }

            return buf
        }
    }
}

// ###############################################################################

fun ByteArray.toUnsignedIntWithBigEndian( start: Int, length: Int ): Int {
    var num = 0
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or ( this[idx].toInt() and 255 )
    }

    return num
}

fun ByteArray.toUnsignedIntWithLittleEndian( start: Int, length: Int ): Int {
    var num = 0
    var keta = 0
    for( idx in start..( start + length - 1 )) {
        num = num or (( this[idx].toInt() and 255 ) shl ( keta * 8 ))
        keta++
    }

    return num
}

fun ByteArray.toIntWithBigEndian( start: Int, length: Int ): Int {
    var num = 0
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or if( idx == 0 ) {
            ( this[idx].toInt()         )
        } else {
            ( this[idx].toInt() and 255 )
        }
    }

    return num
}

fun ByteArray.toIntWithLittleEndian( start: Int, length: Int ): Int {
    var num = 0
    var keta = 0
    for( idx in start..( start + length - 1 )) {
        num = num or if( idx == start + length - 1 ) {
            (( this[idx].toInt()         ) shl ( keta * 8 ))
        } else {
            (( this[idx].toInt() and 255 ) shl ( keta * 8 ))
        }
        keta++
    }

    return num
}

fun ByteArray.toUnsignedLongWithBigEndian( start: Int, length: Int ): Long {
    var num = 0L
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or ( this[idx].toLong() and 255 )
    }

    return num
}

fun ByteArray.toUnsignedLongWithLittleEndian( start: Int, length: Int ): Long {
    var num  = 0L
    var keta = 0
    for( idx in start..( start + length - 1 )) {
        num = num or (( this[idx].toLong() and 255 ) shl ( keta * 8 ))
        keta++
    }

    return num
}

fun ByteArray.toText( start: Int, length: Int ): String {
    val sb = StringBuilder( length )
    for( idx in start..( start + length - 1 )) {
        sb.append( this[idx].toChar())
    }

    return sb.toString()
}

