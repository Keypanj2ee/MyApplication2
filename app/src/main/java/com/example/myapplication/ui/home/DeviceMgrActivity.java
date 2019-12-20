package com.example.myapplication.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;

public class DeviceMgrActivity extends AppCompatActivity {

    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_mgr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("设备管理");
        setSupportActionBar(toolbar);

        //判断父activity是否为空，如果不为空，则返回图标显示.
        if(NavUtils.getParentActivityName(DeviceMgrActivity.this) != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        View add_device_view = LinearLayout.inflate(DeviceMgrActivity.this,R.layout.activity_add_device,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加设备");

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DeviceMgrActivity.this,"你点击了保存按钮",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DeviceMgrActivity.this, "你点击了取消按钮", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(add_device_view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
    }

}
