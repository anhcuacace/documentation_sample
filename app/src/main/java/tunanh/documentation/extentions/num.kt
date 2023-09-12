package tunanh.documentation.extentions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.getTimeFormat(format: String):String{
    val date=Date(this)
    val sdf=SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(date)
}