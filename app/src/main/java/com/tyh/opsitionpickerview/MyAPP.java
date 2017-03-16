package com.tyh.opsitionpickerview;

import android.app.Application;



public class MyAPP extends Application {
    private DBManager dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        //导入数据库
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
//        dbHelper.closeDatabase();
    }
}
