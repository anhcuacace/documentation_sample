package tunanh.documentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tunanh.documentation.R
import tunanh.documentation.data.DocumentData
import tunanh.documentation.data.DocumentType
import tunanh.documentation.databinding.ItemDocumentBinding
import tunanh.documentation.extentions.getTimeFormat
import tunanh.documentation.util.AppConstant

class DocumentAdapter(private val listener: DocumentAdapterCallBack):RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    private val listDocumentData= mutableListOf<DocumentData>()

    inner class DocumentViewHolder( private val binding: ItemDocumentBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val data=listDocumentData[position]
            binding.apply {
                icon.setImageResource(
                    when (data.type){
                        DocumentType.PDF -> R.drawable.pdf
                        DocumentType.Txt -> R.drawable.txt
                        DocumentType.MSWord -> R.drawable.doc
                        DocumentType.Unknown -> R.drawable.unknown
                    }
                )
                myTextViewTitle.text=data.name
                myTextViewSubTitle.text = data.dateModifier.getTimeFormat(AppConstant.FORMAT_TIME_INFO)
                root.setOnClickListener{
                    listener.onCLick(data)
                }
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<DocumentData>){
        listDocumentData.clear()
        listDocumentData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val item=ItemDocumentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DocumentViewHolder(item)
    }

    override fun getItemCount(): Int =listDocumentData.size

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(position)
    }
    interface DocumentAdapterCallBack{
        fun onCLick(data: DocumentData)
    }
}