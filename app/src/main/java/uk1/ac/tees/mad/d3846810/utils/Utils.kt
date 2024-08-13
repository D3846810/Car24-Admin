package uk1.ac.tees.mad.d3846810.utils

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.Navigation
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogProgressBinding

class Utils {
    companion object {

        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        fun navigate(view: View, id: Int) {
            Navigation.findNavController(view).navigate(id)
        }

//        fun isConnected(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            return when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
//                    val activeNetwork = connectivityManager.activeNetwork ?: return false
//                    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//                    when {
//                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                        else -> false
//                    }
//                }
//                else -> {
//                    val activeNetwork = connectivityManager.activeNetworkInfo ?: return false
//                    return when(activeNetwork.type) {
//                        ConnectivityManager.TYPE_WIFI -> true
//                        ConnectivityManager.TYPE_VPN -> true
//                        ConnectivityManager.TYPE_MOBILE -> true
//                        else -> false
//                    }
//                }
//            }
//        }

        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        fun showLoading(context: Context): AlertDialog {
            val progress = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
            val processLayout = DialogProgressBinding.inflate(LayoutInflater.from(context))
            progress.setView(processLayout.root)
            progress.setCancelable(false)
            return progress
        }

        fun showMessage(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun shareTextToWhatsApp(context: Context, text: String) {
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")

            whatsappIntent.putExtra(Intent.EXTRA_TEXT, text)

            try {
                context.startActivity(whatsappIntent)
            } catch (e: ActivityNotFoundException) {

                Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }
        }



        fun copyTextToClipboard(context: Context, text: String) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text label", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Copied Successfully", Toast.LENGTH_SHORT).show()
        }

        fun shareText(context: Context, text: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            context.startActivity(Intent.createChooser(shareIntent, "Share Text"))
        }




        fun rateUs(context: Context) {
            try {
                val marketUri = Uri.parse("market://details?id=${context.packageName}")
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                context.startActivity(marketIntent)
            } catch (e: ActivityNotFoundException) {
                val marketUri =
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                context.startActivity(marketIntent)
            }
        }

    }

}