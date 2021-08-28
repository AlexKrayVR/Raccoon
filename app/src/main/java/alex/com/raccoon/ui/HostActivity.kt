package alex.com.raccoon.ui

import alex.com.raccoon.R
import alex.com.raccoon.common.logDebug
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import android.provider.Settings
import androidx.core.view.isVisible


class HostActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)


    }


}