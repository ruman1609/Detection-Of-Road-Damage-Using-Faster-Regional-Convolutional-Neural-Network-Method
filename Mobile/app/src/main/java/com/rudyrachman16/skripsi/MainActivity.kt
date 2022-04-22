package com.rudyrachman16.skripsi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rudyrachman16.skripsi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_PERMISSION = 1609

        @JvmStatic
        var accepted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }

    fun setTitleToolbar(title: String) {
        supportActionBar?.title = title
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val checkPermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
        val granted = PackageManager.PERMISSION_GRANTED
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (ActivityCompat
                    .checkSelfPermission(this, checkPermissions[0]) != granted
            ) {
                accepted = false
                ActivityCompat.requestPermissions(
                    this, arrayOf(checkPermissions[0], checkPermissions[1]), REQUEST_CODE_PERMISSION
                )
            } else accepted = true
            if (ActivityCompat
                    .checkSelfPermission(this, checkPermissions[2]) != granted
            ) {
                accepted = false
                ActivityCompat.requestPermissions(
                    this, arrayOf(checkPermissions[2]), REQUEST_CODE_PERMISSION
                )
            } else accepted = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_MEDIA_LOCATION
                    ) != granted
                ) {
                    accepted = false
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION),
                        REQUEST_CODE_PERMISSION
                    )
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}