package tunanh.documentation.util.storage.io

abstract class BaseFileIO {
    abstract fun write(bytes: ByteArray)
    abstract fun close()
}