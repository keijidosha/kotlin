package util

class ByteExtension {}

// ###############################################################################
fun Int.toBytesLE( length: Int ) = toBytesWithLittleEndian( length )

fun Int.toBytesWithLittleEndian( length: Int ): ByteArray {
    val buf = ByteArray( length )
    var remain = this

    repeat( length ) { idx ->
        val value = remain % 256
        buf[idx] = value.toByte()
        remain = remain shr 8
    }

    return buf
}

fun Long.toBytesLE( length: Int ) = toBytesWithLittleEndian( length )

fun Long.toBytesWithLittleEndian( length: Int ): ByteArray {
    val buf = ByteArray( length )
    var remain = this

    repeat( length ) { idx ->
        val value = remain % 256
        buf[idx] = value.toByte()
        remain = remain shr 8
    }

    return buf
}

fun Int.toBytesBE( length: Int ) = toBytesWithBigEndian( length )

fun Int.toBytesWithBigEndian( length: Int ): ByteArray {
    val buf = ByteArray( length )
    var remain = this
    val maxIdx = length - 1

    repeat( length ) { idx ->
        val value = remain % 256
        buf[maxIdx - idx] = value.toByte()
        remain = remain shr 8
    }

    return buf
}

fun Long.toBytesBE( length: Int ) = toBytesWithBigEndian( length )

fun Long.toBytesWithBigEndian( length: Int ): ByteArray {
    val buf = ByteArray( length )
    var remain = this
    val maxIdx = length - 1

    repeat( length ) { idx ->
        val value = remain % 256
        buf[maxIdx - idx] = value.toByte()
        remain = remain shr 8
    }

    return buf
}

// ###############################################################################
fun ByteArray.toUintBE( start: Int, length: Int ) = toUnsignedIntWithBigEndian( start, length )

fun ByteArray.toUnsignedIntWithBigEndian( start: Int, length: Int ): Int {
    var num = 0
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or ( this[idx].toInt() and 255 )
    }

    return num
}

fun ByteArray.toUintLE( start: Int, length: Int ) = toUnsignedIntWithLittleEndian( start, length )

fun ByteArray.toUnsignedIntWithLittleEndian( start: Int, length: Int ): Int {
    var num = 0
    var keta = 0
    for( idx in start..( start + length - 1 )) {
        num = num or (( this[idx].toInt() and 255 ) shl ( keta * 8 ))
        keta++
    }

    return num
}

fun ByteArray.toIntBE( start: Int, length: Int ) = toIntWithBigEndian( start, length )

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

fun ByteArray.toIntLE( start: Int, length: Int ) = toIntWithLittleEndian( start, length )

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

fun ByteArray.toUlongBE( start: Int, length: Int ) = toUnsignedLongWithBigEndian( start, length )

fun ByteArray.toUnsignedLongWithBigEndian( start: Int, length: Int ): Long {
    var num = 0L
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or ( this[idx].toLong() and 255 )
    }

    return num
}

fun ByteArray.toUlongLE( start: Int, length: Int ) = toUnsignedLongWithLittleEndian( start, length )

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
    // Null文字を除去してから文字列に変換
    var len = 0
    for( idx in ( start + length - 1) downTo start ) {
        if( this[idx] != 0x00.toByte()) {
            len = idx - start + 1
            break
        }
    }

    if( len == 0 ) return ""

    val sb = StringBuilder( len )
    for( idx in start..( start + len - 1 )) {
        sb.append( this[idx].toChar())
    }

    return sb.toString()
}
