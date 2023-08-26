package tunanh.documentation.extentions

import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.findFolder
import java.io.File

fun DocumentFile.findFileFromSub(subPath: String): DocumentFile? {
    val subSplit = subPath.split(File.separator)
    var nextParent: DocumentFile? = this
    subSplit.forEachIndexed { index, name ->
        nextParent = if (nextParent != null) {
            if (index < subSplit.size - 1) {
                nextParent!!.findFolder(name)
            } else {
                nextParent!!.findFile(name)
            }

        } else {
            return null
        }
    }
    return nextParent
}

fun String.validateTreeDocumentFilePath(): String {
    return this.replace(File.separator, "%2F").replace(" ", "%20")
}

fun DocumentFile.findBasePath(basePath: String): DocumentFile? {
    val subPaths = basePath.split(File.separator)
    var curSubDocFile: DocumentFile? = this
    if (basePath.isNotEmpty()) {
        subPaths.forEachIndexed { index, subFolderName ->
            if (curSubDocFile != null) {
                curSubDocFile = if (index < subPaths.size - 1) {
                    curSubDocFile!!.findFolder(subFolderName)
                } else {

                    return curSubDocFile!!.findFile(subFolderName) ?: curSubDocFile!!.findFolder(
                        subFolderName
                    )
                }

            }
        }
    }
    return curSubDocFile
}


