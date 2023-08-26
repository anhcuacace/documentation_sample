package tunanh.documentation.util.storage

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.SimpleStorage
import com.anggrayudi.storage.extension.isTreeDocumentFile
import com.anggrayudi.storage.file.*
import tunanh.documentation.data.DocumentLoader
import tunanh.documentation.extentions.getDocumentFile
import tunanh.documentation.extentions.validateTreeDocumentFilePath
import tunanh.documentation.util.Utils
import tunanh.documentation.util.Utils.isAndroidQ
import tunanh.documentation.util.Utils.isAndroidR
import java.io.File
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

object StorageUtils {
    fun checkIfSDCardRoot(uri: Uri): Boolean {
        return isExternalStorageDocument(uri) && isRootUri(uri) && !isInternalStorage(uri)
    }

    private fun isRootUri(uri: Uri): Boolean {
        val docId = DocumentsContract.getTreeDocumentId(uri)
        return docId.endsWith(":")
    }

    private fun isInternalStorage(uri: Uri): Boolean {
        return isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri)
            .contains("primary")
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }


    private fun externalStorageRootAccessIntent(context: Context): Intent {
        return if (isAndroidQ()) {
            val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        } else {
            SimpleStorage.getDefaultExternalStorageIntent(context)
        }
    }


    fun launchGrantSdCardPermission(
        context: Context,
        storageId: String,
        grantAccessLauncher: ActivityResultLauncher<Intent>,
        callBack: () -> Unit,
    ) {
        val intent = getSdCardAccessIntent(context, storageId)
        grantAccessLauncher.launch(intent)

    }


    fun isFullPermission(context: Context, storageId: String): Boolean {
        val rootDoc = DocumentFileCompat.getRootDocumentFile(context, storageId)
        return if (rootDoc != null && !rootDoc.inSdCardStorage(context)) {
            true
        } else {
            checkGrantForStorageId(context, storageId)
        }
    }

    fun checkGrantForStorageId(context: Context, storageId: String): Boolean {
        return if (isAndroidQ()) {
            true
        } else {
            val grantedUris = context.contentResolver.persistedUriPermissions
                .filter { it.isReadPermission && it.isWritePermission && it.uri.isTreeDocumentFile }
                .map {
                    val uriPath = it.uri.path!!
                    val storageIdGranted = uriPath.substringBefore(':').substringAfterLast('/')
                    if (storageId == storageIdGranted) {
                        val rootFolder = uriPath.substringAfter(':', "")
                        if (storageIdGranted == StorageId.PRIMARY) {
                            "${Environment.getExternalStorageDirectory()}/$rootFolder"
                        } else {
                            "/storage/$storageIdGranted/$rootFolder"
                        }
                    } else ""
                }
                .filter { it.isNotEmpty() }
            grantedUris.isNotEmpty()
        }

    }


    private fun getSdCardAccessIntent(context: Context, storageId: String): Intent {
        return sdCardRootAccessIntent(context, storageId)
    }

    private fun sdCardRootAccessIntent(context: Context, storageId: String): Intent {
        val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        return sm.storageVolumes.firstOrNull {
            it.isRemovable && (
                    it.uuid == storageId
                    )
        }?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.createOpenDocumentTreeIntent()
            } else {
                if (it.isPrimary) {
                    SimpleStorage.getDefaultExternalStorageIntent(context)
                } else {
                    @Suppress("DEPRECATION")
                    it.createAccessIntent(null)
                }

            }
        } ?: SimpleStorage.getDefaultExternalStorageIntent(context)
    }

    fun getDocumentFile(context: Context, path: String): DocumentFile? {
        val file = File(path)
        val documentFile = DocumentFile.fromFile(file)
        val isFromSdCard = documentFile.inSdCardStorage(context)
        return if (isFromSdCard) {
            if (isAndroidQ()) {
                documentFile
            } else {
                val rootUriTree = getTreeUriFromStorageId(
                    context, file.getStorageId(context)
                )
                if (rootUriTree != null) {
                    var parentDocument = rootUriTree.getDocumentFile(context)
                    val parts: List<String> = path.split(Regex("/"))
                    for (i in 3 until parts.size) {
                        if (parentDocument != null) {
                            parentDocument = parentDocument.findFile(parts[i])
                        }
                    }
                    parentDocument
                } else {
                    documentFile
                }
            }

        } else {
            documentFile
        }

    }

    fun getTreeUriFromStorageId(context: Context, filterStorageId: String): Uri? {
        val grantedUris = context.contentResolver.persistedUriPermissions
            .filter { it.isReadPermission && it.isWritePermission && it.uri.isTreeDocumentFile }
        val uriPermissions = grantedUris.filter {
            val uriPath = it.uri.path!!
            val storageId = uriPath.substringBefore(':').substringAfterLast('/')
            filterStorageId == storageId
        }
        return if (uriPermissions.isNotEmpty()) {
            uriPermissions[0].uri
        } else {
            null
        }
    }

    fun getOperationDocFileFromFile(
        context: Context,
        rootParent: (String) ->String,
        file: File
    ): DocumentFile? {
        return if (isAndroidR()) {
            val curRootParentFileToEdit = Utils.getRootParentFolder(context, file)
            val rootParentFolderUriPath =
                rootParent(
                    curRootParentFileToEdit.absolutePath
                )
            if (rootParentFolderUriPath.isNotEmpty()) {
                val basePath = file.getBasePath(context)
                DocumentFileCompat.fromUri(
                    context,
                    Uri.parse(
                        rootParentFolderUriPath.plus("%2F").plus(
                            basePath.substringAfter(File.separator)
                                .validateTreeDocumentFilePath()
                        )
                    )
                )
            } else {
                null
            }
        } else {
            if (isFullPermission(context, file.getStorageId(context))) {
                file.toUri().getDocumentFile(context)
            } else {
                null
            }
        }
    }

    fun createFile(context: Context, file: File, mimeType: String?, size: Long = 0): Uri? {
        val now = Date()
        val fileCollection = if (isAndroidQ()) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, file.name)
            put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
            put(MediaStore.Files.FileColumns.SIZE, size)
            put(MediaStore.Files.FileColumns.DATE_ADDED, now.time / 1000)
            put(MediaStore.Files.FileColumns.DATE_MODIFIED, now.time / 1000)
            if (isAndroidQ()) {
                put(
                    MediaStore.Files.FileColumns.RELATIVE_PATH,
                    file.parentFile!!.getBasePath(context)
                )
            } else {
                put(MediaStore.Files.FileColumns.TITLE, file.name)
                put(MediaStore.Files.FileColumns.DATA, file.absolutePath.toString())
            }
        }
        return try {
            var uri = getUriFromFile(context, file)
            if (uri == null) {
                uri = context.contentResolver.insert(fileCollection, contentValues)
            }
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateInfo(
        context: Context,loader: DocumentLoader,
        uriPath: String
    ): Boolean {
        var success = false
        val uri = Uri.parse(uriPath)
        val id = try {
            ContentUris.parseId(uri)
        } catch (ex: Exception) {
            -1L
        }

        val fileCollection = if (isAndroidQ()) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Files.getContentUri("external")
        }
        val projection =
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATA,
            )
        val where: String
        val args: Array<String?>
        if (id != -1L) {
            where = MediaStore.Files.FileColumns._ID + " =? "
            args = arrayOf(
                id.toString()
            )
        } else {
            where = MediaStore.Files.FileColumns.DATA + " =? "
            args = arrayOf(
                uriPath
            )
        }
        loader.createCursor(
            context.contentResolver,
            fileCollection,
            projection,
            where
        ).use { cursor ->
            cursor?.let {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val pathColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

                while (cursor.moveToNext()) {
                    val path =
                        loader.getPathFromCursor(
                            context,
                            cursor,
                            uri,
                            idColumn,
                            pathColumn
                        )
                            ?: continue
                    val file = File(path)
                    if (!file.exists() or file.isHidden or file.isDirectory) continue
                    val now = Date()
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Files.FileColumns.SIZE, file.length())
                        put(MediaStore.Files.FileColumns.DATE_MODIFIED, now.time / 1000)
                    }
                    success = try {
                        context.contentResolver.update(
                            fileCollection,
                            contentValues,
                            where,
                            args
                        )
                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
                cursor.close()
            }
        }
        return success
    }


    private fun getUriFromFile(context: Context, file: File): Uri? {
        return MediaStoreHack.getUriFromFile(file.absolutePath, context)
    }

    fun humanReadableByteCountBin(bytes: Long, factor: Float = 1024f): String {
        return if (bytes < factor) {
            "$bytes B"
        } else {
            val result: Int = (ln(bytes.toDouble()) / ln(factor)).toInt()
            if (result > 0) {
                String.format("%.1f%c", bytes / factor.pow(result), "KMGTPE"[result - 1])
                    .replace(",", ".")
            } else {
                "NA"
            }

        }
    }

    fun getHiddenFolder(context: Context): File {
        val file = File(context.filesDir.absolutePath, "Hidden")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    fun getTrashFolder(context: Context): File {
        val file = File(context.filesDir.absolutePath, "Track")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

}