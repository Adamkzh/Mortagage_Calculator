package com.example.adam.test;


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
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Geocoder;
import android.location.Address;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

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
}
