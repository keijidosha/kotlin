package util

import java.io.*
import java.nio.*
import java.nio.channels.*
import java.nio.file.*
import java.nio.file.attribute.*
import java.nio.charset.*

class PathExtension

const val MAX_FILE_SIZE_FOR_READ_TEXT = 715827880

fun Path.renameExtension( newExt: String ) {
    this.move( this.replaceExtensionPath( newExt ))
}

fun Path.replaceExtensionPath( changeExt: String ): Path {
    if( changeExt.length == 0 ) return this

    val fileName = this.fileName.toString()
    val pos = fileName.lastIndexOf( "." )
    if( pos >= 0 ) {
        val newFileName = fileName.substring( 0, pos ) + '.' + changeExt
        return this.resolveSibling( newFileName )
    } else {
        return this
    }
}

val Path.extension: String
    get() = fileName.toString().substringAfterLast( '.', "" )

val Path.nameWithoutExtension: String
    get() = fileName.toString().substringBeforeLast( '.' )

fun Path.mkdirs(): Path {
    if( ! Files.exists( this )) {
        Files.createDirectories( this )
    }
    return this
}

fun Path.touch( vararg attrs: FileAttribute<*> ): Path {
    if( ! Files.exists( this )) {
        Files.createFile( this, *attrs )
    }
    return this
}

fun Path.move( destinationPath: Path ): Path {
    destinationPath.parent.let {
        if( ! it.exists ) {
            it.mkdirs()
        }
    }
    Files.move( this, destinationPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING )
    return destinationPath
}

fun Path.moveToDirectory( destinationDirectory: Path ): Path {
    val destinationPath = destinationDirectory.resolve( this.fileName )
    Files.move( this, destinationPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING )
    return destinationPath
}

fun Path.copy( destinationPath: Path ) {
    Files.copy( this, destinationPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING )
}

fun Path.delete(): Path {
    Files.deleteIfExists( this )
    return this
}

fun Path.reader( charset: String = "UTF-8" ): BufferedReader {
    val cs = Charset.forName( charset )
    return this.reader( cs )
}

fun Path.reader( charset: Charset ): BufferedReader {
    return Files.newBufferedReader( this,  charset )
}

fun Path.inputStream(): InputStream {
    return Files.newInputStream( this )
}

fun Path.outputStream(): OutputStream {
    return Files.newOutputStream( this )
}

fun Path.writer( charset: String = "UTF-8" ): BufferedWriter {
    val cs = Charset.forName( charset )
    return Files.newBufferedWriter( this,  cs, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING )
}

fun Path.writeText( text: String, charset: String = "UTF-8" ): Path {
    this.writer( charset ).use() {
        it.write( text )
        it.flush()
    }

    return this
}

fun Path.readText( charset: Charset = Charsets.UTF_8 ): String {
    val size = Files.size( this ).toInt()
    val sb = StringBuilder( size * 2 )
    val buf = CharArray( 16384 )
    var len = 0
    Files.newBufferedReader( this, charset ).use { reader ->
        while( run { len = reader.read( buf ); len >= 0 } ) {
            sb.append( buf, 0, len )
        }
    }

    return sb.toString()
}

fun Path.readTextWihtoutMalformedException( charset: Charset = Charsets.UTF_8 ): String {
    val size = Files.size( this ).toInt()
    val sb = StringBuilder( size * 2 )
    val buf = CharArray( 16384 )
    var len = 0
    InputStreamReader( FileInputStream( this.toFile()), charset ).use { reader ->
        while( run { len = reader.read( buf ); len >= 0 } ) {
            sb.append( buf, 0, len )
        }
    }

    return sb.toString()
}

fun Path.readTextWihtoutMalformedException2( charset: Charset = Charsets.UTF_8 ): String {
    val size = Files.size( this ).toInt()
    val sb = StringBuilder( size * 2 )
    val buf = CharArray( 8192 )
    var len = 0
    Files.newBufferedReader( this, charset ).use { reader ->
        while( run { len = reader.read( buf ); len >= 0 } ) {
            sb.append( buf, 0, len )
        }
    }

    return sb.toString()
}

fun Path.readTextWihtoutMalformedException3( charset: Charset = Charsets.UTF_8 ): String {
    val size = Files.size( this ).toInt()
    val sb = StringBuilder( size * 2 )
    val bbuf = ByteBuffer.allocateDirect( 4096 )
    val cbuf = CharBuffer.allocate( 4096 )
    val decoder = charset.newDecoder()
    decoder.onMalformedInput( CodingErrorAction.REPLACE )
    decoder.onUnmappableCharacter( CodingErrorAction.REPLACE )
    FileChannel.open( this, StandardOpenOption.READ ).use { fc ->
        var len = fc.read( bbuf )
        while( len >= 0 ) {
            bbuf.flip()
            val coderResult = decoder.decode( bbuf, cbuf, false )
            if( coderResult.isMalformed() || coderResult.isUnmappable()) coderResult.throwException()
            cbuf.flip()
            sb.append( cbuf, cbuf.position(), cbuf.remaining())

            bbuf.compact()

            len = fc.read( bbuf )
        }

    }

    return sb.toString()
}

fun Path.glob( glob: String ): DirectoryStream<Path> {
    return Files.newDirectoryStream( this, glob )
}

fun Path.glob( filter: DirectoryStream.Filter<Path> ): DirectoryStream<Path> {
    return Files.newDirectoryStream( this, filter )
}

val Path.exists: Boolean
    get() = Files.exists( this )

val Path.isDirectory: Boolean
    get() = Files.isDirectory( this )

val Path.size: Long
    get() = Files.size( this )

val Path.lastModifiedMillis: Long
    get() = Files.getLastModifiedTime( this ).toMillis()
