package com.msr.dbcopy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.msr.dbcopy.R;
import com.msr.dbcopy.db.Database;

public class MainActivity extends AppCompatActivity {
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);
        int count = db.getRecords();
        Log.i("===Count", "====Count::" + count);
        boolean res = db.updateUser();
        Log.i("===Update", "====Update::" + res);
        db.displayRecords();
    }
}
