package tunanh.documentation.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anggrayudi.storage.file.getBasePath
import com.anggrayudi.storage.file.getRootPath
import com.google.android.material.snackbar.Snackbar
import tunanh.documentation.R
import java.io.File

object Utils {
     val STORAGE_PERMISSION_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
    fun isAndroidR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun checkPermissionStorage(context: Context):Boolean= allPermissionGrant(context, STORAGE_PERMISSION_STORAGE_SCOPE)

    private fun allPermissionGrant(context: Context, intArray: Array<String>): Boolean {
        var isGranted = true
        for (permission in intArray) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false
                break
            }
        }
        return isGranted
    }


    fun showSnackBarOpenSetting( permissions: Array<out String?>,activity: Activity,view: View) {
        Handler(Looper.getMainLooper()).post {
            if (!hasShowRequestPermissionRationale(activity, *permissions)) {
                val snackBar = Snackbar.make(
                    view,
                    activity.getString(R.string.goto_settings),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAction(
                    activity.getString(R.string.settings)
                ) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                }
                snackBar.show()
            } else {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.grant_permission),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
    private fun hasShowRequestPermissionRationale(
        activity: Activity,
        vararg permissions: String?,
    ): Boolean {

        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission!!
                )
            ) {
                return true
            }
        }

        return false
    }
    private fun isInternalStorage(uri: Uri): Boolean {
        return isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri)
            .contains("primary")
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun getRootParentFolder(context: Context, file: File): File {
        val basePath = file.getBasePath(context)
        val rootParentFolder = basePath.substringBefore(File.separator)
        return File(file.getRootPath(context), rootParentFolder)
    }

}