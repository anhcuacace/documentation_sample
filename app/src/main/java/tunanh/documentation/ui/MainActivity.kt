package tunanh.documentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tunanh.documentation.R
import tunanh.documentation.data.DocumentData
import tunanh.documentation.data.DocumentLoader
import tunanh.documentation.databinding.ActivityMainBinding
import tunanh.documentation.util.Utils

class MainActivity : AppCompatActivity(), DocumentAdapter.DocumentAdapterCallBack {
    private lateinit var binding:ActivityMainBinding
    private val documentData=MutableLiveData<List<DocumentData>>()
    private val adapter:DocumentAdapter by lazy {
        DocumentAdapter(this)
    }
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult.all { it.value }) {
                observer()
                fetchData()
            } else {
                Utils.showSnackBarOpenSetting(
                    Utils.STORAGE_PERMISSION_STORAGE_SCOPE,this,binding.root
                )
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (Utils.checkPermissionStorage(this)){
            observer()
            fetchData()
        }else{
            binding.layout.isActivated=false
        }
    }
    private fun observer(){
        binding.apply {
            layout.isActivated=true
            layout.setOnRefreshListener {
                fetchData()
            }
            documentData.observe(this@MainActivity){
                adapter.updateData(it)
                binding.layout.isRefreshing= false
            }
        }
    }
    private fun fetchData(){
        binding.layout.isRefreshing=true
        lifecycleScope.launch(Dispatchers.Default){
            try {
                documentData.postValue(DocumentLoader.getInstant().getAllDocument(this@MainActivity))
            }catch (e:Exception){
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    binding.layout.isRefreshing=false
                }
            }

        }

    }

    override fun onCLick(data: DocumentData) {

    }

}