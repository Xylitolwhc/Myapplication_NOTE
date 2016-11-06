package com.example.whc.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 吴航辰 on 2016/11/3.
 */

public class ThirdActivity extends AppCompatActivity {
    private TextView title,content,createdata,enddata;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase sql;
    Intent show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_layout);
        ActionBar ABar=getSupportActionBar();
        ABar.setDisplayHomeAsUpEnabled(true);

        title=(TextView)findViewById(R.id.show_title);
        content=(TextView)findViewById(R.id.show_content);
        createdata=(TextView)findViewById(R.id.show_create_data);
        enddata=(TextView)findViewById(R.id.show_end_data);
        dbHelper=new MyDatabaseHelper(this,"NOTE.db",null,1);
        sql=dbHelper.getWritableDatabase();
        show=getIntent();
        String p=show.getStringExtra("id");

        if (show.hasExtra("id")) {
            Cursor cs = sql.query("note", null, null, null, null, null, null, null);
            if (cs.moveToFirst()) {
                do {
                    Log.d("ThirdActivity",cs.getString(cs.getColumnIndex("_id")).toString()+" P="+p);
                    if (cs.getString(cs.getColumnIndex("_id")).toString().equals(p)) {
                        title.setText(cs.getString(cs.getColumnIndex("title")));
                        content.setText(cs.getString(cs.getColumnIndex("content")));
                        createdata.setText("创建日期："+cs.getString(cs.getColumnIndex("createdata")));
                        enddata.setText("截止日期："+cs.getString(cs.getColumnIndex("enddata")));
                    }
                } while (cs.moveToNext());
            }
            cs.close();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.third, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                finish();
                break;
            }

            case R.id.bt_edit:{
                Intent it=new Intent(ThirdActivity.this,SecondActivity.class);
                it.putExtra("edit","yes");
                it.putExtra("id",show.getStringExtra("id").toString());
                startActivity(it);
                finish();
                break;
            }
            case R.id.bt_del:{
                sql.delete("note","_id = ?",new String[]{show.getStringExtra("id").toString()});
                Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
        return true;
    }
}
