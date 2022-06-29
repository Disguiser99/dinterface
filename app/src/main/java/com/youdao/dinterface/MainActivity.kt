package com.youdao.dinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.youdao.dinterface.core.testAJumpDInterface
import com.youdao.dinterface.core.testAJumpManager
import com.youdao.testa.SecondActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testAJumpManager.setInterface(object: testAJumpDInterface {
               override fun startActivity() {
                   Log.e("testAJumpManager", "hello")
               }

               override fun stopActivity() {

               }

        })

        startActivity(Intent(this, SecondActivity::class.java))

    }
}