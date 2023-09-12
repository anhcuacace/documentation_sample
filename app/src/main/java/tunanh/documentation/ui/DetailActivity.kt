package tunanh.documentation.ui

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.anggrayudi.storage.extension.postToUi
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xwpf.usermodel.XWPFDocument
import tunanh.documentation.R
import tunanh.documentation.data.DocumentData
import tunanh.documentation.data.DocumentType
import tunanh.documentation.databinding.ActivityDetailBinding
import tunanh.documentation.ui.MainActivity.Companion.KeyData
import tunanh.documentation.util.Utils
import tunanh.documentation.xs.extensions.IOffice
import tunanh.documentation.xs.officereader.AppFrame
import java.io.File
import java.io.FileInputStream


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private  var appFrame: AppFrame? =null
    private var iOffice: IOffice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        @Suppress("DEPRECATION") val data =
            if (Utils.isTIRAMISU()) intent.extras?.getParcelable(KeyData, DocumentData::class.java)
            else intent.extras?.getParcelable(KeyData) as? DocumentData

        iOffice = object : IOffice() {
            override fun isPopUpErrorDlg(): Boolean {

                return true
            }

            override fun getActivity(): Activity {
                return this@DetailActivity
            }

            override fun getAppName(): String {
                return getString(R.string.app_name)
            }

            override fun error(i: Int) {

            }

            override fun getTemporaryDirectory(): File {

                return getExternalFilesDir(null) ?: filesDir
            }

            override fun openFileFinish() {
                val view: View = iOffice!!.getView()
                val layoutParams = LinearLayout.LayoutParams(-1, -1)
                layoutParams.setMargins(
                    0,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2.0f,
                        getResources().displayMetrics
                    ).toInt(),
                    0,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        2.0f,
                        getResources().displayMetrics
                    ).toInt()
                )
                appFrame?.addView(view, layoutParams)
            }

            override fun changePage(i: Int, i2: Int) {

            }
        }
        appFrame = AppFrame(applicationContext)

        binding.apply {
            toolbar.title = data?.name ?: ""
            toolbar.setNavigationOnClickListener {
                finish()
            }

            progressCircular.isVisible = true
            lifecycleScope.launch(Dispatchers.Main) {
                val text = withContext(Dispatchers.IO) {
                    if (data?.path?.let { File(it).exists() } == true) {
                        when (data.type) {
                            DocumentType.PDF -> {
                                postToUi {
                                    binding.scrollView.isVisible=true
                                    binding.llmainframe.isVisible=false
                                }
                                try {
                                    var text=""
                                    val pdfReader=PdfReader(data.path)
                                    for (i in 1..pdfReader.numberOfPages)
                                        text+=PdfTextExtractor.getTextFromPage(pdfReader,i).plus("\n\n")
                                    text
                                }catch (e:Exception){
                                    e.printStackTrace()
                                    ""
                                }

                            }

                            else -> {
//                                loadDoc(File(data.path))
                                postToUi {
                                    appFrame!!.post {
                                        iOffice!!.openFile(data.path)
                                    }
                                    binding.scrollView.isVisible=false
                                    binding.llmainframe.isVisible=true
                                    binding.llmainframe.addView(appFrame)
                                }

                                ""
                            }
                        }
                    } else ""
                }
                content.text = text
                progressCircular.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        iOffice?.dispose()
        super.onDestroy()
    }

    private fun loadDoc(file: File): String {
        var text = ""
        try {
            val doc = XWPFDocument(FileInputStream(file))
            for (paragraph in doc.paragraphs) {
                text += paragraph.text + "\n\n"
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return text
    }

//    private fun loadPDf(file: File): String {
//        var parsedText = ""
//
//        val document: PDDocument? = try {
//            PDDocument.load(FileInputStream(file))
//        } catch (e: IOException) {
//            Log.e("PdfBox-Android-Sample", "Exception thrown while loading document to strip", e)
//            null
//        }
//        try {
//            val pdfStripper = PDFTextStripper()
//            pdfStripper.startPage=0
//            pdfStripper.endPage=1
//            parsedText = document?.let { pdfStripper.getText(it) } ?: ""
//        } catch (e: Exception) {
//            Log.e("PdfBox-Android-Sample", "Exception thrown while stripping text", e)
//        } finally {
//            try {
//                document?.close()
//            } catch (e: IOException) {
//                Log.e("PdfBox-Android-Sample", "Exception thrown while closing document", e)
//            }
//        }
//        return parsedText
//    }

}