package com.example.whc.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private ListView listView;
    private Cursor cursor;

    SimpleCursorAdapter sca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("FirstActivity","onCreate");
        setContentView(R.layout.activity_main);
        dbHelper=new MyDatabaseHelper(this,"NOTE.db",null,1);
        SQLiteDatabase sqd=dbHelper.getWritableDatabase();
        cursor=sqd.rawQuery("SELECT * FROM note",null);
        sca=new SimpleCursorAdapter(this,R.layout.list_item,cursor,new String[]{"_id","title","content","createdata","enddata","priority"},
                new int[]{R.id.i_id,R.id.showtitle,R.id.showcontent,R.id.showcreatedata,R.id.showenddata,R.id.showpriority});
        listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(sca);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("FirstActivity","onResume");
        cursor.requery();
        sca.swapCursor(null);
        sca.swapCursor(cursor);
        listView.setAdapter(sca);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                Intent it=new Intent(MainActivity.this,ThirdActivity.class);
                TextView tv=(TextView)((ViewGroup)view1).getChildAt(0);
                it.putExtra("id",tv.getText().toString());
                startActivity(it);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent ADD=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(ADD);
                break;
            default:
        }
        return true;
    }
}

