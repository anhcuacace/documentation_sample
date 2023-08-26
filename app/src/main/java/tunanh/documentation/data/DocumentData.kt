package tunanh.documentation.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentData(
    val name:String,
    val path:String,
    val uri: Uri,
    val type: DocumentType
):Parcelable
