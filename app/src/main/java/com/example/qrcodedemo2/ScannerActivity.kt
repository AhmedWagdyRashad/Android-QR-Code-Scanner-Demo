package com.example.qrcodedemo2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 123
        const val TAG = "ScannerActivity"
    }

    private lateinit var scannerView: CodeScannerView
    private lateinit var codeScanner: CodeScanner
    private var QRCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        setupPermissions()
        scanCode()

        if (savedInstanceState != null){
            QRCode = savedInstanceState.get("QR_Code").toString()
            qr_txt.text = QRCode
        }
        // to scan a new QR code
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        back_btn.setOnClickListener {
            if (QRCode.isNotEmpty())
            backWithResult(QRCode)
        }
    }

    private fun scanCode() {
        // Parameters (default values)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@ScannerActivity,
                        "Scan result: ${it.text}",
                        Toast.LENGTH_LONG
                    ).show()
                    qr_txt.text = it.text
                    QRCode = it.text

                }
            }
            errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Toast.makeText(
                        this@ScannerActivity, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun backWithResult(result: String) {
        val intent = Intent()
        intent.putExtra("QR_Code", result)
        setResult(Activity.RESULT_OK, intent)
        Log.i(TAG, "backWithResult: $result")
        finish()
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (QRCode.isNotEmpty() && QRCode != null){
            outState.putString("QR_Code",QRCode)
        }
    }

    // Permissions Methods

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            baseContext,
            Manifest.permission.CAMERA
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "you need to the camera permission to be abel to use this app!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                }
            }
        }
    }

}