package tunanh.documentation.util.storage.io

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import tunanh.documentation.util.storage.io.BaseFileIO
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class FileWritableInSdCardAccessIO(context: Context, treeUri: Uri) : BaseFileIO() {
    private var pfdOutput: ParcelFileDescriptor? = null
    private var fos: FileOutputStream? = null
    private var position: Long = 0

    init {
        pfdOutput = context.contentResolver.openFileDescriptor(treeUri, "rw")
        fos = FileOutputStream(pfdOutput!!.fileDescriptor)
    }

    override fun write(bytes: ByteArray) {
        try {
            val fch: FileChannel = fos!!.channel
            fch.position(position)
            fch.write(ByteBuffer.wrap(bytes))
            position = fch.position()

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun close() {
        val fch = fos?.channel
        fch?.close()
        fos?.close()
        pfdOutput!!.close()
    }
}