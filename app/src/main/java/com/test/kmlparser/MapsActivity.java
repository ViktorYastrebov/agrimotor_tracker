package com.test.kmlparser;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import android.graphics.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;

// http://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
// https://www.programcreek.com/java-api-examples/?api=com.google.android.gms.maps.GoogleMap.OnMapClickListener
// https://github.com/googlemaps/android-samples/blob/master/ApiDemos/java/app/src/main/java/com/example/mapdemo/CameraDemoActivity.java

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private KMLParser parser;
    private TrackModel trackModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parser = new KMLParser("Doslidnicke.kml", this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //https://developers.google.com/maps/documentation/android-sdk/shapes
        //https://stackoverflow.com/questions/13728041/move-markers-in-google-map-v2-android
        //https://stackoverflow.com/questions/40526350/how-to-move-marker-along-polyline-using-google-map/40686476
        // https://stackoverflow.com/questions/34879193/circle-animation-on-google-map-in-android
        //https://stackoverflow.com/questions/35718103/how-to-specify-the-size-of-the-icon-on-the-marker-in-google-maps-v2-android

        //https://stackoverflow.com/questions/3519529/scale-map-markers-by-zoom-android

        // https://stackoverflow.com/questions/16211783/how-to-add-basic-grids-to-googlemap-api-v2

        trackModel = new TrackModel(mMap,this, R.drawable.track_final_2);
        if (!parser.getData().isEmpty()) {
            PolygonOptions first = parser.getData().get(0);
            if(!first.getPoints().isEmpty()) {
                LatLng firstLat = first.getPoints().get(0);
                Log.d("Debug", "lat :" + firstLat.toString());

                trackModel.initialize(firstLat);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLat));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            } else {
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                Log.d("Debug", "First Polygon is empty, use default one");
            }

            for (PolygonOptions p: parser.getData()) {
                Log.d("polygon", "Polygon :" + p.getPoints().toString());
                p.strokeColor(Color.RED);
                p.strokeWidth(2.0f);
                if(!p.getPoints().isEmpty()) {
                    mMap.addPolygon(p);
                } else {
                    Log.d("Debug","Polygon is empty");
                }
            }
        } else {
            LatLng current = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(current).title("Kiev"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

            Log.d("Debug", "getData is Empty !!!");
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                trackModel.moveTo(latLng);
                Log.d("Debug", "Click point: " +
                        Double.toString(latLng.latitude) + ", " +
                        Double.toString(latLng.longitude));
             }
        });
    }
}
