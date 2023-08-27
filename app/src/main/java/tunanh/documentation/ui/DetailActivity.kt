package tunanh.documentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
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
import java.io.File
import java.io.FileInputStream


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        @Suppress("DEPRECATION") val data =
            if (Utils.isTIRAMISU()) intent.extras?.getParcelable(KeyData, DocumentData::class.java)
            else intent.extras?.getParcelable(KeyData) as? DocumentData

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
                                loadDoc(File(data.path))
                            }
                        }
                    } else ""
                }
                content.text = text
                progressCircular.isVisible = false
            }
        }
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