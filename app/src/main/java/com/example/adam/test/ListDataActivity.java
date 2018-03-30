package com.example.adam.test;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import android.widget.Toast;
import android.app.Dialog;
import android.app.AlertDialog;

/**
 * Created by Forrest on 3/26/18.
 */

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mydb = new DatabaseHelper(this);

    Button clearButton;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        mListView = (ListView) findViewById(R.id.listView);
        populateListView();

        clearButton = (Button) findViewById(R.id.bt_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.clearDatabase();
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = mydb.getAllData();

        while(data.moveToNext()){
            listData.add(data.getString(0));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

    }
}
