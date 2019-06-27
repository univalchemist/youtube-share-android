package com.cynny.videoface.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.cynny.videoface.R
import com.cynny.videoface.ui.misc.ShareListener
import com.cynny.videoface.ui.misc.service.ShareService
import com.cynny.videoface.ui.misc.utils.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject
import javax.inject.Provider
import org.jsoup.Jsoup
import java.io.IOException
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.net.Uri
import androidx.appcompat.app.AlertDialog


class MainActivity : DaggerAppCompatActivity(), ShareListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModelProvider: Provider<MainViewModel>

    @Inject
    lateinit var shareService: ShareService

    private val viewModel by lazy {
        viewModelFactory(this, viewModelProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        checkUpdateVersion()
        viewModel.shareMessageLiveEvent.observe(this, this)
        onNewIntent(intent)
    }

    private fun checkUpdateVersion() {

        val future = doAsync {
            val storeVersion = getPlayStoreVersion()
            val currentVersion = getCurrentVersion()
            Log.e("ligwang", "storeVersion:::" + storeVersion)
            Log.e("ligwang", "currentVersion:::" + currentVersion)
            uiThread {
                // use result here if you want to update ui
                if (!currentVersion.equals(storeVersion)) {
                    Log.e("ligwang", "new version is available to update on store")
                    showDialog()
                }
            }
        }
    }

    private fun getPlayStoreVersion(): String? {
        var newVersion: String? = null

        try {
            val document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.cynny.videoface" + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
            if (document != null) {
                val element = document.getElementsContainingOwnText("Current Version")
                for (ele in element) {
                    if (ele.siblingElements() != null) {
                        val sibElemets = ele.siblingElements()
                        for (sibElemet in sibElemets) {
                            newVersion = sibElemet.text()
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return newVersion
    }

    private fun getCurrentVersion(): String? {
        var version: String? = null
        val manager = packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        assert(info != null)
        version = info!!.versionName
        return version
    }

    private fun showDialog() {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("New version is available on the Store.\nDo you want to install now?")
                    .setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->
                                openStore()
                            })
            // Create the AlertDialog object and return it
            builder.create()
        }
        alertDialog?.show()
    }
    private fun openStore() {
        val appPackageName = packageName
        try {
            val appStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            appStoreIntent.setPackage("com.cynny.videoface")
            startActivity(appStoreIntent)
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cynny.videoface")))
        }

    }

    override fun onNewIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.run {
                viewModel.onExternalShare(this)
            }
        }
    }

    override fun onShare(url: String) {
        shareService.share(url, this)
                .subscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
