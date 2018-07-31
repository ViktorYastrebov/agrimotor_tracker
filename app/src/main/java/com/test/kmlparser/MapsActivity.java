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

    private GroundOverlay tractor;
    private PolylineOptions polylineOptions;
    private Polyline polyline;

   private final String testsCoords[] = {
            "coord_test1.txt",
            "coord_test2.txt"
    };
    private List<Pair<Double, Integer>> testScales;
    private List<Tracker> trackers;


    /*
    class MyView extends View {
        public MyView(Context ctx)
        {
            super(ctx);
        }
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawLine(0.0f,0.0f, 500.0f, 500.0f, paint);
        }
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parser = new KMLParser("Doslidnicke.kml", this);

        polylineOptions = new PolylineOptions();
        polylineOptions.width(4.0f);

/*
        trackers = new ArrayList<Tracker>();
        for(int i =0; i < testsCoords.length; ++i) {
            TestCoordsParser data  = new TestCoordsParser(testsCoords[i], this);
            Tracker tracker = new Tracker();
            tracker.addPoints(data.getCoords());
            trackers.add(tracker);
        }
        testScales = new ArrayList<Pair<Double, Integer>>();
        testScales.add(new Pair<Double, Integer>(
                //orange
                new Double(0.001), new Integer(Color.rgb(220,20,60))
        ));
        testScales.add(new Pair<Double, Integer>(
                //lime
                new Double(0.0001), new Integer(Color.rgb(127,255,0))
        ));
        testScales.add(new Pair<Double, Integer>(
                //
                new Double(0.00007), new Integer(Color.rgb(255,215,0))
        ));
        testScales.add( new Pair<Double, Integer>(
                //lightseagreen
                new Double(0.00001), new Integer(Color.rgb(2,178,170))
        ));
        */

        //setContentView(new MyView(this));
    }

    /*private void TestScales() {
        for(Tracker trackerIt : trackers) {

            PolygonOptions origin = new PolygonOptions();
            origin.addAll(trackerIt.getPoints());
            origin.strokeColor(Color.BLUE);
            mMap.addPolygon(origin);

            for(Pair<Double, Integer> scale : testScales) {
                TrackScaler trackScaler = new TrackScaler(scale.first);
                PolygonOptions po = new PolygonOptions();
                trackerIt.accept(trackScaler);
                po.addAll(trackScaler.getScaledPoints());

                po.strokeColor(scale.second);
                mMap.addPolygon(po);
            }
        }
    }*/



    private BitmapDescriptor getIconFromFile(Context ctx) {

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.track_final_2);
        //Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 120,120,false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
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

        if (!parser.getData().isEmpty()) {
            PolygonOptions first = parser.getData().get(0);
            if(!first.getPoints().isEmpty()) {
                LatLng firstLat = first.getPoints().get(0);
                LatLng testLat = first.getPoints().get(1);
                Log.d("Debug", "lat :" + firstLat.toString());

                BitmapDescriptor icon = getIconFromFile(this);
                tractor = mMap.addGroundOverlay(new GroundOverlayOptions().image(icon).position(firstLat, 10));
                tractor.setBearing(40.0f);

                polylineOptions.add(firstLat);
                polyline = mMap.addPolyline(polylineOptions);

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
        //TestScales();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                /*if(polylineOptions.getPoints().isEmpty()) {
                    polylineOptions.add(latLng);
                    polyline = mMap.addPolyline(polylineOptions);
                } else {
                    //polylineOptions.add(latLng);
                    polyline.getPoints().add(latLng);
                    //polyline.setPoints(polylineOptions.getPoints());
                }*/
                polylineOptions.add(latLng);
                polyline.setPoints(polylineOptions.getPoints());

                //polyline.setPoints();
                tractor.setPosition(latLng);

                Log.d("Debug", "Click point: " +
                        Double.toString(latLng.latitude) + ", " +
                        Double.toString(latLng.longitude));
             }
        });
    }
}
