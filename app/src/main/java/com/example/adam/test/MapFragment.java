package com.example.adam.test;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Geocoder;
import android.location.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    //local variable
    DatabaseHelper mydb;
    GoogleMap map;
    HashMap<String, Marker> hashMapMaker = new HashMap<>();

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mydb = new DatabaseHelper(getContext());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = mydb.getAllData();

        while(data.moveToNext()){
            String address = "";
            address += data.getString(0);
            address += data.getString(1);
            address += data.getString(2);
            address += data.getString(3);

            LatLng location = getLocationFromAddress(this.getContext(), address);
            if (location != null) {
                Marker mymarker = map.addMarker(new MarkerOptions().position(location).title(data.getString(0)));
                hashMapMaker.put(data.getString(0),mymarker);
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        property_detail(marker.getTitle());
                        return false;
                    }
                });
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13));
            }
        }
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

    private void property_detail(final String key){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Property Detail");

        ListView modeList = new ListView(this.getContext());
        ArrayList<String> listData = new ArrayList<>();
        Cursor data = mydb.getRecord(key);
        while(data.moveToNext()){
            listData.add("Property Type: " + data.getString(4));
            listData.add("Street Address: " + data.getString(0));
            listData.add("City: " + data.getString(1));
            double amount = Double.valueOf(data.getString(5)) - Double.valueOf(data.getString(6));
            String result = String.format("%.2f", amount);
            listData.add("Loan amount: $" + result);
            listData.add("APR: " + data.getString(7) + "%");
            listData.add("Monthly payment: $" + data.getString(9));
        }
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listData);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydb.deleteRow(key);
                Marker mymarker = hashMapMaker.get(key);
                mymarker.remove();
                hashMapMaker.remove(key);
                Toast.makeText(getActivity(),"Property Removed!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //扩展
                dialog.dismiss();
            }
        });
        final Dialog dialog = builder.create();

        dialog.show();
    }

}
