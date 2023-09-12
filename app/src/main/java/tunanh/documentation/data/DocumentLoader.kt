package tunanh.documentation.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import tunanh.documentation.util.Utils.isAndroidQ
import tunanh.documentation.util.storage.PathUtils

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
    private val docType = arrayOf(
        ".PDF",
        ".TXT",
        ".DOC",
        ".DOCX",
        ".ODT",
        ".RTF",
        ".XPS",
        ".XLS",
        ".XLSX",
        ".CSV",
        ".ODS",
        ".XLR",
        ".PPT",
        ".PPTX",
        ".PPSX",
        ".PPS",
        ".ODP",
        ".ZIP",
        ".RAR"
    )
@Synchronized
    fun getAllDocuments(context: Context) :List<DocumentData>{
        val list= mutableListOf<DocumentData>()
        var sb: StringBuilder
        var str = ""
        for (i in this.docType.indices) {
            if (i == 0) {
                sb = StringBuilder()
                sb.append("UPPER(substr(_data,LENGTH(_data) - ")
                sb.append(this.docType[i].length - 1)
                sb.append(",LENGTH(_data))) = ?")
            } else {
                sb = StringBuilder()
                sb.append(str)
                sb.append(" OR UPPER(substr(_data,LENGTH(_data) - ")
                sb.append(this.docType[i].length - 1)
                sb.append(",LENGTH(_data))) = ?")
            }
            str = sb.toString()
        }
        Log.e("===!",str)
        Log.e("===!",docType.toString())
        context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf("_id", "_data", "date_added", "media_type", "mime_type", "title", "_size","_display_name","date_modified"),
            str,
            this.docType,
            "date_modified DESC"
        )?.use {
            if (it.moveToFirst()){
            val nameColumns = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val timeColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
            val pathColumn = if (!isAndroidQ()) it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) else 0
                do{
                    val name = it.getString(nameColumns) ?: "Unknown"
                    val path =
                        getPathFromCursor(context, it, MediaStore.Files.getContentUri("external"), idColumn, pathColumn)
                            ?: ""
                    val id = it.getLong(idColumn)
                    val type = when (it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))) {
                        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                            DocumentType.MSWord

                        "application/pdf" -> DocumentType.PDF

                        "text/plain" -> DocumentType.Txt

                        else -> {
                            continue
                        }

                    }
                    val time = it.getLong(timeColumn)
                    list.add(
                        DocumentData(
                            name,
                            path,
                            ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id),
                            type,
                            time
                        )
                    )
                }while (it.moveToNext())
            }

            it.close()
        }
        return list
    }



//    private fun getMimeType(name: String): String {
//
//        val mimeTypeMap = mapOf(
//            ".doc" to "application/msword",
//            ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//            ".pdf" to "application/pdf",
//            ".txt" to "text/plain"
////            ".ppt" to "application/vnd.ms-powerpoint",
////            ".pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
////            ".xls" to "application/vnd.ms-excel",
////            ".xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
////            ".jpg" to "image/jpeg",
////            ".png" to "image/png"
//
//        )
//        val lastDot = name.lastIndexOf(".")
//        if (lastDot < 0) {
//            return ""
//        }
//
//        val extension = name.substring(lastDot)
//        return mimeTypeMap[extension] ?: ""
//    }
}