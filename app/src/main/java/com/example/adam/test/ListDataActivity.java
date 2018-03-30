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
    Button selectButton;

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
        selectButton = (Button) findViewById(R.id.bt_del);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property_detail();
//                mydb.deleteRow("1279 38 th Ave");
//                Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();
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

    private void property_detail(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Property Detail");

        ListView modeList = new ListView(this);
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = mydb.getRecord("1279 38 th Ave");
        while(data.moveToNext()){
            listData.add("House Type: " + data.getString(4));
            listData.add("Address: " + data.getString(0));
            listData.add("City: " + data.getString(1));
            listData.add("State: " + data.getString(2));
            listData.add("Zipcode: " + data.getString(3));
            listData.add("Price: $" + data.getString(5));
            listData.add("Payment: $" + data.getString(6));
            listData.add("APR: " + data.getString(7) + "%");
            listData.add("Term: " + data.getString(8));
            listData.add("Monthly payment: $" + data.getString(9));
        }
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listData);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydb.deleteRow("1279 38 th Ave");
                Toast.makeText(getApplicationContext(),"Good",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog dialog = builder.create();

        dialog.show();
    }
}
