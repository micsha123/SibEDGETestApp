package com.sibedge.sibedgetestapp.map;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.sibedge.sibedgetestapp.R;

public class MapFragment extends Fragment {
    GPSTracker mGps;
    private TextView mPositionText;
    MapView mMapView;
    private GoogleMap mGoogleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mPositionText = (TextView) rootView.findViewById(R.id.position_text);
        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mGps = new GPSTracker(getActivity());
        double latitude = 0;
        double longitude = 0;
        if(mGps.canGetLocation()){
            latitude = mGps.getLatitude();
            longitude = mGps.getLongitude();
            mPositionText.setText("Latitude: " + Double.toString(latitude) +
                    ", longitude: " + Double.toString(longitude));
        }else{
            mGps.showSettingsAlert();
        }
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();

        MarkerOptions mMarker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("You're here!");

        mMarker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        mGoogleMap.addMarker(mMarker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        return rootView;
    }
}