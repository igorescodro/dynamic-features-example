package com.escodro.dynaaminen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val manager: SplitInstallManager by lazy { SplitInstallManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        updateDynamicFeatureButtonState()
        button_app_download.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                .addModule(DYNAMIC_MODULE_NAME)
                .build()

            manager.registerListener {
                when (it.status()) {
                    SplitInstallSessionStatus.DOWNLOADING -> showToast("Downloading feature")
                    SplitInstallSessionStatus.INSTALLED -> {
                        showToast("Feature ready to be used")
                        updateDynamicFeatureButtonState()
                    }
                    else -> {/* Do nothing in this example */
                    }
                }
            }
            manager.startInstall(request)
        }

        button_app_open.setOnClickListener {
            val intent = Intent()
            intent.setClassName(BuildConfig.APPLICATION_ID, "com.escodro.ondemand.OnDemandActivity")
            startActivity(intent)
        }
    }

    private fun updateDynamicFeatureButtonState() {
        button_app_open.isEnabled = manager.installedModules.contains(DYNAMIC_MODULE_NAME)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val DYNAMIC_MODULE_NAME = "ondemand"
    }
}
