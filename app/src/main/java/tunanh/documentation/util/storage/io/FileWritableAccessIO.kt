package tunanh.documentation.util.storage.io


import tunanh.documentation.util.storage.io.BaseFileIO
import java.io.IOException
import java.io.RandomAccessFile

class FileWritableAccessIO(private val mRandomAccessFile : RandomAccessFile) : BaseFileIO() {
    override fun write(bytes: ByteArray) {
        return try {
            mRandomAccessFile.write(bytes)
        } catch (exception: IOException) {
            throw exception
        }
    }

    override fun close() {
        mRandomAccessFile.close()
    }
}