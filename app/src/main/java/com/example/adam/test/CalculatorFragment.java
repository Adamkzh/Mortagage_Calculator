package com.example.adam.test;


import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class  CalculatorFragment extends Fragment {

    //State variable
    String current_address = "";
    String current_city = "";
    String current_state = "";
    String current_zipcode = "";
    String current_type = "";
    String current_price = "";
    String current_payment = "";
    String current_apr = "";
    String current_terms = "";
    String current_result = "-1";

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
    private Button new_button;
    private Button save_button;

    //DB
    DatabaseHelper mDatabaseHelper;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_calculator, container, false);

        mDatabaseHelper = new DatabaseHelper(getContext());

        //Spinner & RadioGroup
        house_type_spinner = (Spinner) myFragmentView.findViewById(R.id.house_type_spinner);
        house_type_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.house_type, android.R.layout.simple_spinner_item);
        house_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        house_type_spinner.setAdapter(house_type_adapter);

        state_type_spinner = (Spinner) myFragmentView.findViewById(R.id.state_type_spinner);
        state_type_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.state_type, android.R.layout.simple_spinner_item);
        state_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_type_spinner.setAdapter(state_type_adapter);

        year_group = myFragmentView.findViewById(R.id.radio_group);

        //EditText
        show_result=(EditText) myFragmentView.findViewById(R.id.result_text);
        show_result.setCursorVisible(false);

        show_address=(EditText) myFragmentView.findViewById(R.id.show_address);
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

        show_city=(EditText) myFragmentView.findViewById(R.id.show_city);
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

        show_zip=(EditText) myFragmentView.findViewById(R.id.show_zip);
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

        show_price=(EditText) myFragmentView.findViewById(R.id.show_price);
        show_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_price.getText().length() == 0) {
                    show_price.setError("Please input a valid price!");
                    price_lock = false;
                    do_button.setEnabled(false);
                } else {
                    final String text = show_price.getText().toString();
                    try {
                        final int num = Integer.parseInt(text);
                        if (num < 0) {
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
                    } catch (NumberFormatException e) {
                        show_price.setError("Please input a valid price!");
                        price_lock = false;
                        do_button.setEnabled(false);
                    }
                }
            }
        });

        show_downpayment=(EditText) myFragmentView.findViewById(R.id.show_downpayment);
        show_downpayment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_downpayment.getText().length() == 0) {
                    show_downpayment.setError("Please input a valid down payment!");
                    downpayment_lock = false;
                    do_button.setEnabled(false);
                } else {
                    final String text = show_downpayment.getText().toString();
                    try {
                        final int num = Integer.parseInt(text);
                        if (num < 0) {
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
                    } catch (NumberFormatException e) {
                        show_downpayment.setError("Please input a valid down payment!");
                        downpayment_lock = false;
                        do_button.setEnabled(false);
                    }
                }
            }
        });

        show_apr=(EditText) myFragmentView.findViewById(R.id.show_apr);
        show_apr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (show_apr.getText().length() == 0) {
                    show_apr.setError("Please input a valid apr!");
                    apr_lock = false;
                    do_button.setEnabled(false);
                } else {
                    final String text = show_apr.getText().toString();
                    try {
                        final double num = Double.parseDouble(text);
                        if (num < 0 || num > 100) {
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
                    } catch (NumberFormatException e) {
                        show_apr.setError("Please input a valid apr!");
                        apr_lock = false;
                        do_button.setEnabled(false);
                    }
                }
            }
        });

        //Button
        do_button = (Button) myFragmentView.findViewById(R.id.do_calculate);
        do_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price_temp = show_price.getText().toString();
                if (price_temp.length() == 0) {
                    show_result.setText("Valid input required!");
                    return;
                }
                String downpayment_temp = show_downpayment.getText().toString();
                if (downpayment_temp.length() == 0) {
                    show_result.setText("Valid input required!");
                    return;
                }
                double amount = Double.parseDouble(price_temp) - Double.parseDouble(downpayment_temp);
                if (amount < 0) {
                    show_result.setText("Negative present value!");
                    return;
                }

                //monthly rate
                String apr_temp = show_apr.getText().toString();
                if (apr_temp.length() == 0) {
                    show_result.setText("Valid input required!");
                    return;
                }
                double rate = Double.parseDouble(apr_temp)/1200;

                //term and period from radio group
                int radioId = year_group.getCheckedRadioButtonId();
                year_radio = getActivity().findViewById(radioId);
                double period = 0;
                if (year_radio.getText().toString().equals("15 years")) {
                    period = 180;
                } else {
                    period = 360;
                }

                //calculation
                double temp = Math.pow(rate + 1, period);
                double result = Math.round(amount * (rate * temp) / (temp - 1)*100.0)/100.0;

                current_address = show_address.getText().toString();
                current_city = show_city.getText().toString();
                current_state = state_type_spinner.getSelectedItem().toString();
                current_zipcode = show_zip.getText().toString();
                current_type = house_type_spinner.getSelectedItem().toString();
                current_price = show_price.getText().toString();
                current_payment = show_downpayment.getText().toString();
                current_apr = show_apr.getText().toString();
                current_terms = year_radio.getText().toString();
                current_result = String.valueOf(result);
                show_result.setText("$" + current_result);

            }
        });

        new_button = (Button) myFragmentView.findViewById(R.id.new_calculate);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house_type_spinner.setSelection(0);
                show_address.setText("");
                show_city.setText("");
                state_type_spinner.setSelection(0);
                show_zip.setText("");

                show_price.setText("");
                show_downpayment.setText("");
                show_apr.setText("");

                do_button.setEnabled(false);
                save_button.setEnabled(false);

                current_address = "";
                current_city = "";
                current_state = "";
                current_zipcode = "";
                current_type = "";
                current_price = "";
                current_payment = "";
                current_apr = "";
                current_terms = "";
                current_result = "-1";

                show_result.setText("");
            }
        });

        save_button = (Button) myFragmentView.findViewById(R.id.save_result);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_result != "-1") {

                    String address = "";
                    address += current_address;
                    address += current_city;
                    address += current_state;
                    address += current_zipcode;

                    LatLng location = getLocationFromAddress(getContext(), address);
                    if (location != null) {
                        boolean insertData = mDatabaseHelper.addData(current_address, current_city,
                                current_state, current_zipcode, current_type, current_price,
                                current_payment, current_apr, current_terms, current_result);
                        if (insertData) {
                            show_result.setText("Data saved!");
                        } else {
                            show_result.setText("We failed!");
                        }
                    } else {
                        show_result.setText("No such location!");
                    }
                } else {
                    show_result.setText("Calculate first!");
                }

            }
        });

        return myFragmentView;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }
}
