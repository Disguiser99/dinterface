package com.youdao.testa;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.youdao.dinterface.core.testAJumpManager;

/**
 * 有道精品课
 * dinterface
 * Description:
 * Created by Disguiser on 2022/4/18 16:39
 * Copyright @ 2022 网易有道. All rights reserved.
 **/
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testAJumpManager.getInterface().startActivity();
    }


}
