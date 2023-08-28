package tunanh.documentation.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import tunanh.documentation.util.Utils
import tunanh.documentation.util.Utils.isAndroidQ
import tunanh.documentation.util.storage.PathUtils
import java.io.File
import java.util.*

class DocumentLoader private constructor() {

    companion object {
        private var instance: DocumentLoader? = null
        fun getInstant(): DocumentLoader = instance ?: synchronized(this) {
            instance ?: DocumentLoader()
                .also { instance = it }
        }
    }

    fun createCursor(
        contentResolver: ContentResolver,
        collection: Uri,
        projection: Array<String>?,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Cursor? = when {
        isAndroidQ() -> {
            contentResolver.query(collection, null, null, null, "date_added DESC")
        }

        else -> {
            contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                "date_added DESC"
            )
        }
    }
    fun doc(file: File=Environment.getExternalStorageDirectory()): ArrayList<File> {
        val fileArrayList = ArrayList<File>()
        val files = file.listFiles()
        if (files != null) {
            for (singleFile in files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    fileArrayList.addAll(doc(singleFile))
                } else {
                    if (!singleFile.isHidden() && singleFile.getName().lowercase(Locale.getDefault())
                            .endsWith(".doc") || singleFile.getName().lowercase(
                            Locale.getDefault()
                        ).endsWith(".docx") || singleFile.getName().lowercase(Locale.getDefault())
                            .endsWith(".xls") || singleFile.getName().lowercase(
                            Locale.getDefault()
                        ).endsWith(".xlsx") || singleFile.getName().lowercase(Locale.getDefault())
                            .endsWith(".txt") || singleFile.getName().lowercase(
                            Locale.getDefault()
                        ).endsWith(".ppt") || singleFile.getName().lowercase(Locale.getDefault()).endsWith(".pdf")
                    ) {
                        fileArrayList.add(singleFile)
                    }
                }
            }
        }
        return fileArrayList
    }

    fun getPathFromCursor(
        context: Context,
        cursor: Cursor,
        uri: Uri,
        idColumn: Int,
        pathColumn: Int
    ): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                uri,
                id
            )
            PathUtils.getPath(context, contentUri)
        } else {
            cursor.getString(pathColumn)
        }
    }

    @Synchronized
    fun getAllDocument(context: Context):List<DocumentData> {
        val list = mutableListOf<DocumentData>()
        if (!Utils.isAndroidR()){
            return doc().flatMap {
                val list= mutableListOf<DocumentData>()
                val name= it.name
                val dateModifier=System.currentTimeMillis()

                 val data=DocumentData(name,it.absolutePath,Uri.fromFile(it), when (getMimeType(name)){
                "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                    DocumentType.MSWord

                "application/pdf" -> DocumentType.PDF

                "text/plain" -> DocumentType.Txt

                else -> {
                    DocumentType.Unknown
                }
            },dateModifier)
                list.add(data)
                list
            }
        }

        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ? or "+
                MediaStore.Files.FileColumns.MIME_TYPE + " = ? or "+
                MediaStore.Files.FileColumns.MIME_TYPE + " = ? or "+
                MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
//        val mimeTypes = arrayOf(
//            "application/msword",
//            "application/pdf",
//            "text/plain"
//            ,"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//        )
        val mimeTypes= arrayOf(MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf").orEmpty(),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc").orEmpty(),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt").orEmpty(),
            MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx").orEmpty())

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATE_MODIFIED
        )

        val fileCollection = if (isAndroidQ()) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }

        createCursor(
            context.contentResolver,
            fileCollection,
            projection,
            selection,mimeTypes
        )?.use {
            val nameColumns= it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val timeColumn = it.getColumnIndexOrThrow(projection[3])
            val pathColumn=if (!isAndroidQ())it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)else 0
            while (it.moveToNext()){
                val name= it.getString(nameColumns)?:"Unknown"
                val path= getPathFromCursor(context, it, fileCollection, idColumn, pathColumn)?:""
                val id=it.getLong(idColumn)
                val type= when (it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))){
                    "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                        DocumentType.MSWord

                    "application/pdf" -> DocumentType.PDF

                    "text/plain" -> DocumentType.Txt

                    else -> {
                        continue
                    }

                }
                val time = it.getLong(timeColumn)
                list.add(DocumentData(name,path,ContentUris.withAppendedId(fileCollection,id),type,time))
            }
            it.close()
        }
        return list
    }

    private fun getMimeType(name: String): String {

        val mimeTypeMap = mapOf(
            ".doc" to "application/msword",
            ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            ".pdf" to "application/pdf",
            ".txt" to "text/plain"
//            ".ppt" to "application/vnd.ms-powerpoint",
//            ".pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
//            ".xls" to "application/vnd.ms-excel",
//            ".xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//            ".jpg" to "image/jpeg",
//            ".png" to "image/png"

        )
        val lastDot = name.lastIndexOf(".")
        if (lastDot < 0) {
            return ""
        }

        val extension = name.substring(lastDot)
        return mimeTypeMap[extension] ?: ""
    }
}