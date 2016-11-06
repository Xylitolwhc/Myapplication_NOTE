package com.example.whc.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Date;

import static java.sql.Types.NULL;

/**
 * Created by 吴航辰 on 2016/11/3.
 */

public class SecondActivity extends AppCompatActivity{
    private MyDatabaseHelper dbHelper;
    private EditText note_title,note_content;
    private boolean ifedit=false;
    private SQLiteDatabase sql;
    private NumberPicker np;
    Intent it_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_layout);
        ActionBar ABar=getSupportActionBar();
        ABar.setDisplayHomeAsUpEnabled(true);

        note_title=(EditText)findViewById(R.id.note_title);
        note_content=(EditText)findViewById(R.id.note_content);
        dbHelper=new MyDatabaseHelper(this,"NOTE.db",null,1);
        np=(NumberPicker)findViewById(R.id.Pick_priority);
        np.setMaxValue(10);
        np.setMinValue(1);
        it_1=getIntent();
        if (it_1.hasExtra("edit")) {
            sql=dbHelper.getWritableDatabase();
            if (it_1.getStringExtra("edit").equals("yes")) {
                Log.d("SecondActivity", "edit");
                ifedit=true;
                String p=it_1.getStringExtra("id");
                if (it_1.hasExtra("id")) {
                    Cursor cs = sql.query("note", null, null, null, null, null, null, null);
                    if (cs.moveToFirst()) {
                        do {
                            Log.d("SecondActivity",cs.getString(cs.getColumnIndex("_id")).toString());
                            if (cs.getString(cs.getColumnIndex("_id")).toString().equals(p)) {
                                note_title.setText(cs.getString(cs.getColumnIndex("title")));
                                note_content.setText(cs.getString(cs.getColumnIndex("content")));
                                np.setValue(cs.getInt(cs.getColumnIndex("priority")));
                            }
                        } while (cs.moveToNext());
                    }
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                showDialog();
                break;
            }

            case R.id.ok_item:{
                Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show();
                if (ifedit){
                    String title = note_title.getText().toString(), content = note_content.getText().toString();
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("content", content);
                    values.put("enddata",new java.util.Date().toString());
                    sql.update("note",values,"_id = ?",new String[]{it_1.getStringExtra("id").toString()});
                    finish();
                }else {
                    String title = note_title.getText().toString(), content = note_content.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("content", content);
                    values.put("createdata", new java.util.Date().toString());
                    values.put("enddata", new java.util.Date().toString());
                    values.put("priority", np.getValue());
                    db.insert("note", null, values);
                    values.clear();
                    finish();
                    break;
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1){
                switch (resultCode)
                {
                    case RESULT_OK:{
                        Log.d("result","yes");
                        finish();
                        break;
                    }
                    case RESULT_CANCELED:{
                        Log.d("result","no");
                        break;
                    }
                }
        }
    }

    private void showDialog(){
        AlertDialog.Builder y_or_n=new AlertDialog.Builder(SecondActivity.this);
        y_or_n.setTitle("你确定要返回吗？");
        y_or_n.setMessage("你可能会丢失尚未保存的内容！");
        y_or_n.setCancelable(true);
        y_or_n.setPositiveButton("是", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        y_or_n.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        y_or_n.show();
    }
    @Override
    public void onBackPressed() {
        showDialog();
    }
}
