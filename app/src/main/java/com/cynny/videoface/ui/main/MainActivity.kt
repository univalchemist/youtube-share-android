package com.cynny.videoface.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.cynny.videoface.R
import com.cynny.videoface.ui.misc.ShareListener
import com.cynny.videoface.ui.misc.service.ShareService
import com.cynny.videoface.ui.misc.utils.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

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

        viewModel.shareMessageLiveEvent.observe(this, this)
        onNewIntent(intent)
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
