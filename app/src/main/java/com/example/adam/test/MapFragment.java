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

    DatabaseHelper mydb;

    GoogleMap map;
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
            map.addMarker(new MarkerOptions().position(location).title(data.getString(0)));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    property_detail(marker.getTitle());
                    return false;
                }
            });
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13));
        }

//        LatLng address = getLocationFromAddress(this.getContext(), "1279 38th Ave, San Francisco, CA, 94122");
//        map.addMarker(new MarkerOptions().position(address).title("1279 38th Ave"));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address,13));
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
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listData);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydb.deleteRow(key);
                Toast.makeText(getActivity(),"Good",Toast.LENGTH_SHORT).show();
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
