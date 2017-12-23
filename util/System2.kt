package util

import java.io.*

import kotlin.concurrent.*

fun sleep( millisec: Long ) = try { Thread.sleep( millisec ) } catch( ex: InterruptedException ) {}

class System2 {
    companion object {
        val osName = java.lang.System.getProperty( "os.name" )
        val isLinux = osName.toLowerCase().startsWith( "linux" )
        private val streamReader: ( InputStream, StringBuilder ) -> Unit = { ins, sb ->
            ins.reader().use() {
                it.useLines {
                    it.forEach {
                        sb.append( it ).append( "\r\n ")
                    }
                }
            }
        }

        fun runProcess( vararg param: String ): RunProcInfo {
            val proc = Runtime.getRuntime().exec( param )
            val sbOut = StringBuilder()
            val sbErr = StringBuilder()

            val outThread = thread{ streamReader( proc.inputStream, sbOut ) }
            val errThread = thread{ streamReader( proc.errorStream, sbErr ) }

            proc.waitFor()
            outThread.join()
            errThread.join()

            proc.outputStream.close()
            proc.destroy()

            return RunProcInfo( proc.exitValue(), sbOut.toString(), sbErr.toString())
        }

        fun runProcess( pb: ProcessBuilder ): RunProcInfo {
            val proc = pb.start()
            val sbOut = StringBuilder()
            val sbErr = StringBuilder()

            val outThread = thread{ streamReader( proc.inputStream, sbOut ) }
            val errThread = thread{ streamReader( proc.errorStream, sbErr ) }

            proc.waitFor()
            outThread.join()
            errThread.join()

            proc.outputStream.close()
            proc.destroy()

            return RunProcInfo( proc.exitValue(), sbOut.toString(), sbErr.toString())
        }

        fun startProcess( pb: ProcessBuilder ) = pb.start()

        fun waitProcess( proc: Process ): RunProcInfo {
            val sbOut = StringBuilder()
            val sbErr = StringBuilder()

            val outThread = thread{ streamReader( proc.inputStream, sbOut ) }
            val errThread = thread{ streamReader( proc.errorStream, sbErr ) }

            proc.waitFor()
            outThread.join()
            errThread.join()

            proc.outputStream.close()
            proc.destroy()

            return RunProcInfo( proc.exitValue(), sbOut.toString(), sbErr.toString())

        }
    }

    data class RunProcInfo( val exitCode: Int, val outText: String, val errText: String )
}

