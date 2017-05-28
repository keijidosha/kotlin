fun ByteArray.toIntBigEndian( start: Int, length: Int ): Int {
    var num = 0
    for( idx in start..( start + length - 1 )) {
        num = num shl 8
        num = num or ( this[idx].toInt() and 255 )
    }

    return num
}

fun ByteArray.toIntLittleEndian( start: Int, length: Int ): Int {
    var num = 0
    var keta = 0
    for( idx in start..( start + length - 1 )) {
        num = num or (( this[idx].toInt() and 255 ) shl ( keta * 8 ))
        keta++
    }

    return num
}
