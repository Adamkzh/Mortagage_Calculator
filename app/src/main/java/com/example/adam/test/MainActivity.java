package com.example.adam.test;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Stack;
import java.lang.String;
import java.lang.Math;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v4.view.GravityCompat;
import android.app.Dialog;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //lock
    boolean address_lock = false;
    boolean city_lock = false;
    boolean zip_lock = false;
    boolean price_lock = false;
    boolean downpayment_lock = false;
    boolean apr_lock = false;

    //Spinner & Radio
    Spinner house_type_spinner;
    ArrayAdapter<CharSequence> house_type_adapter;
    Spinner state_type_spinner;
    ArrayAdapter<CharSequence> state_type_adapter;
    RadioGroup year_group;
    RadioButton year_radio;

    //EditText
    private EditText show_result;
    private EditText show_address;
    private EditText show_city;
    private EditText show_zip;
    private EditText show_price;
    private EditText show_downpayment;
    private EditText show_apr;

    //Button
    private Button do_button;
    private Button save_button;

    //Menu
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView()
    {
        //Spinner & RadioGroup
        house_type_spinner = (Spinner)findViewById(R.id.house_type_spinner);
        house_type_adapter = ArrayAdapter.createFromResource(this, R.array.house_type, android.R.layout.simple_spinner_item);
        house_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        house_type_spinner.setAdapter(house_type_adapter);

        state_type_spinner = (Spinner)findViewById(R.id.state_type_spinner);
        state_type_adapter = ArrayAdapter.createFromResource(this, R.array.state_type, android.R.layout.simple_spinner_item);
        state_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_type_spinner.setAdapter(state_type_adapter);

        year_group = findViewById(R.id.radio_group);

        //EditText
        show_result=(EditText) findViewById(R.id.result_text);
        show_result.setCursorVisible(false);

        show_address=(EditText) findViewById(R.id.show_address);
        show_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_address.getText().length() == 0) {
                    show_address.setError("Please input a valid address!");
                    address_lock = false;
                    do_button.setEnabled(false);
                } else {
                    address_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        show_city=(EditText) findViewById(R.id.show_city);
        show_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_city.getText().length() == 0) {
                    show_city.setError("Please input a valid city!");
                    city_lock = false;
                    do_button.setEnabled(false);
                } else {
                    city_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        show_zip=(EditText) findViewById(R.id.show_zip);
        show_zip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_zip.getText().length() != 5) {
                    show_zip.setError("Please input a valid zipcode!");
                    zip_lock = false;
                    do_button.setEnabled(false);
                } else {
                    zip_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        show_price=(EditText) findViewById(R.id.show_price);
        show_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_price.getText().length() == 0) {
                    show_price.setError("Please input a valid price!");
                    price_lock = false;
                    do_button.setEnabled(false);
                } else {
                    price_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        show_downpayment=(EditText) findViewById(R.id.show_downpayment);
        show_downpayment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_downpayment.getText().length() == 0) {
                    show_downpayment.setError("Please input a valid down payment!");
                    downpayment_lock = false;
                    do_button.setEnabled(false);
                } else {
                    downpayment_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        show_apr=(EditText) findViewById(R.id.show_apr);
        show_apr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_apr.getText().length() == 0) {
                    show_apr.setError("Please input a valid apr!");
                    apr_lock = false;
                    do_button.setEnabled(false);
                } else {
                    apr_lock = true;
                    if (price_lock && downpayment_lock && apr_lock) {
                        do_button.setEnabled(true);
                    }

                    if (address_lock && city_lock && zip_lock && price_lock && downpayment_lock && apr_lock) {
                        save_button.setEnabled(true);
                    }
                }
            }
        });

        //Button
        do_button = (Button) findViewById(R.id.do_calculate);
        do_button.setEnabled(false);
        do_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double temp = calculation();
                if (temp == - 1) {
                    show_result.setText("Valid input required!");
                } else {
                    show_result.setText("$" + String.valueOf(temp));
                }

            }
        });
        save_button = (Button) findViewById(R.id.save_result);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_result.setText("Result Saved");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.m_calculator) {
            //at the calculator view
        } else if (id == R.id.m_map) {
            //at the google map view

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkYears(View v) {
        int radioId = year_group.getCheckedRadioButtonId();
        year_radio = findViewById(radioId);

        Toast.makeText(this, "Selected: " + year_radio.getText(), Toast.LENGTH_SHORT).show();
    }

    //Calulation Function Bind with Button;
    public double calculation(){
        //pv
        String price_temp = show_price.getText().toString();
        if (price_temp.length() == 0) {
            return -1;
        }
        String downpayment_temp = show_downpayment.getText().toString();
        if (downpayment_temp.length() == 0) {
            return -1;
        }
        double amount = Double.parseDouble(price_temp) - Double.parseDouble(downpayment_temp);

        //monthly rate
        String apr_temp = show_apr.getText().toString();
        if (apr_temp.length() == 0) {
            return -1;
        }
        double rate = Double.parseDouble(apr_temp)/1200;

        //term and period from radio group
        int radioId = year_group.getCheckedRadioButtonId();
        year_radio = findViewById(radioId);
        double period = 0;
        if (year_radio.getText().toString().equals("15 years")) {
            period = 180;
        } else {
            period = 360;
        }

        //calculation
        double temp = Math.pow(rate + 1, period);
        return Math.round(amount * (rate * temp) / (temp  - 1)*100.0)/100.0;
    }
}
