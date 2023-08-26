package tunanh.documentation.extentions

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.extension.isTreeDocumentFile
import com.anggrayudi.storage.file.*
import tunanh.documentation.util.storage.StorageUtils
import java.io.File

fun Uri.getFreeStorage(context: Context): Long {
    return if (!isTreeDocumentFile) {
        DocumentFileCompat.getFreeSpace(context, StorageId.PRIMARY)
    } else {
        val fileOutputDoc = DocumentFileCompat.fromUri(context, this)
        DocumentFileCompat.getFreeSpace(context, fileOutputDoc!!.getStorageId(context))
    }

}

fun Uri.getRealPath(context: Context): String? {
    return if (!isTreeDocumentFile) {
        File(this.path!!).absolutePath
    } else {
        val fileOutputDoc = DocumentFileCompat.fromUri(context, this)
        fileOutputDoc!!.getAbsolutePath(context)
    }
}

fun Uri.getDocumentFile(context: Context): DocumentFile? {
    return if (this.isTreeDocumentFile) {
        DocumentFileCompat.fromUri(context, this)
    } else {
        val file = File(this.path!!)
        if (file.getStorageId(context) != StorageId.PRIMARY) {
            val uri = StorageUtils.getTreeUriFromStorageId(context, file.getStorageId(context))
            if (uri != null) {
                val rootDoc = DocumentFileCompat.fromUri(
                    context,
                    uri
                )
                rootDoc!!.findBasePath(file.getBasePath(context))
            } else {
                DocumentFile.fromFile(File(this.path!!))
            }
        } else {
            DocumentFile.fromFile(File(this.path!!))
        }
    }
}

fun Uri.getDocFromRoot(
    context: Context,
    pathStack: MutableList<String>? = null,
): DocumentFile? {
    var docFile = DocumentFileCompat.fromUri(context, this)
    if (!pathStack.isNullOrEmpty()) {
        pathStack.forEach {
            if (docFile != null) {
                docFile = docFile!!.findFile(it)
            }
        }
    }
    return docFile
}