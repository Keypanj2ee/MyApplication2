package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.ui.home.FileUtil;
import com.example.myapplication.ui.home.RecognizeService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE = 108;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertDialog = new AlertDialog.Builder(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(null,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

//    private void alertText(final String title, final String message) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                alertDialog.setTitle(title)
//                        .setMessage(message)
//                        .setPositiveButton("确定", null)
//                        .show();
//            }
//        });
//    }
//
//    private void infoPopText(final String result) {
//        alertText("", result);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this,"requestCode:"+requestCode+" resultCode:"+resultCode,Toast.LENGTH_LONG);
//
//        // 识别成功回调，通用文字识别（含位置信息）
//        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recGeneral(this, FileUtil.getSaveFile(this.getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//
//        // 识别成功回调，通用文字识别（含位置信息高精度版）
//        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
//            RecognizeService.recAccurate(this, FileUtil.getSaveFile(this.getApplicationContext()).getAbsolutePath(),
//                    new RecognizeService.ServiceListener() {
//                        @Override
//                        public void onResult(String result) {
//                            infoPopText(result);
//                        }
//                    });
//        }
//    }
}