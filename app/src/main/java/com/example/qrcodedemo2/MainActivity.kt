package com.example.qrcodedemo2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_CODE = 104
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scan_img.setOnClickListener {
            val intent = Intent(this , ScannerActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       /* Log.i(TAG, "onActivityResult: requestCode $requestCode")
        Log.i(TAG, "onActivityResult: resultCode $resultCode  ${Activity.RESULT_OK}")
        Log.i(TAG, "onActivityResult: data ${data?.getStringExtra("QR_Code")}")*/
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val code = data.getStringExtra("QR_Code")
            code_txt.setText(code)
            Log.i(TAG, "onActivityResult: result $code")
        }
    }

}