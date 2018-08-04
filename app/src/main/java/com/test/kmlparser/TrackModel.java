package com.test.kmlparser;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.PolygonOptions;

import com.google.maps.android.SphericalUtil;

public class TrackModel {

    private Context m_owner = null;
    private GoogleMap m_map = null;

    private PolylineOptions m_polylineOptions = null;
    private Polyline m_polyline = null;
    private GroundOverlay tractor = null;
    private int iconId;

    private Polygon boundBox = null;

    public TrackModel(GoogleMap m, Context ctx, int iconId)
    {
        m_owner = ctx;
        m_map = m;
        m_polylineOptions = new PolylineOptions();
        m_polylineOptions.width(4.0f);
        this.iconId = iconId;
    }

    public void initialize(LatLng start_p) {
        if(tractor == null && m_polylineOptions.getPoints().isEmpty()) {
            tractor = m_map.addGroundOverlay(new GroundOverlayOptions()
                    .image(getIconFromFile())
                    .position(start_p, 10)
                    .anchor(0.5f, 0.5f)
            );
            tractor.setBearing(0);
            m_polylineOptions.add(start_p);
            m_polyline = m_map.addPolyline(m_polylineOptions);

            drawBounds();
        }
    }

    private void drawBounds() {
        LatLngBounds bounds = tractor.getBounds();
        PolygonOptions polygonOptions = new PolygonOptions()
                    .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                    .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                    .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                    .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                    .strokeColor(Color.GREEN);

         boundBox = m_map.addPolygon(polygonOptions);
    }
     private void moveBoundBox() {

         LatLngBounds bounds = tractor.getBounds();
         //TODO: Memory usage issue
         List<LatLng> lst = new ArrayList<LatLng>();

         LatLng p1 = new LatLng(bounds.northeast.latitude, bounds.northeast.longitude);
         LatLng p2 = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
         LatLng p3 = new LatLng(bounds.southwest.latitude, bounds.southwest.longitude);
         LatLng p4 = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);
         /*
         m_map.addCircle(new CircleOptions().center(p1).radius(1.0).fillColor(Color.rgb(255,215,0)));  //gold
         m_map.addCircle(new CircleOptions().center(p2).radius(1.0).fillColor(Color.rgb(220,20,60))); //red
         m_map.addCircle(new CircleOptions().center(p3).radius(1.0).fillColor(Color.rgb(0,128,0)));  //green
         m_map.addCircle(new CircleOptions().center(p4).radius(1.0).fillColor(Color.rgb(0,0,128)));  //blue
         */

         lst.add(p1); lst.add(p2); lst.add(p3); lst.add(p4);
         boundBox.setPoints(lst);
     }

    private BitmapDescriptor getIconFromFile() {
        Bitmap bitmap= BitmapFactory.decodeResource(m_owner.getResources(), iconId);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

    public void moveTo(LatLng p) {
        // find a way to optimize it.
        // Something like mMap redraw current m_polyline
        m_polylineOptions.add(p);
        List<LatLng> points =  m_polylineOptions.getPoints();
        m_polyline.setPoints(points);
        tractor.setPosition(p);

        float angle = rotateIcon();
        int size = points.size();
        if( size > 1) {
            LatLng pInit = points.get(size - 1);
            drawGridByDirection(pInit, angle, 5,  2);
        }
        moveBoundBox();
    }
    // seederLength in metter
    private void drawGridByDirection(LatLng p, float angle, int seedersCount, int seederLength) {

         double offset = 0.0d;
         double r = 1.0d;
         if(seedersCount %2 == 1) {
             offset = r/2;
            m_map.addCircle(new CircleOptions().center(p).radius(r).fillColor(Color.GREEN));
         }

        int partCount = seedersCount / 2;
        for(int i =0; i < partCount; ++i) {
            LatLng v = SphericalUtil.computeOffset(p, seederLength *(i+1), 90 + angle);
            m_map.addCircle(new CircleOptions().center(v).radius(r).fillColor(Color.GREEN));
        }
        for(int i =0; i < partCount; ++i) {
            LatLng v = SphericalUtil.computeOffset(p, seederLength *(i+1), -90 + angle);
            m_map.addCircle(new CircleOptions().center(v).radius(r).fillColor(Color.GREEN));
        }
    }

    private float rotateIcon() {
        List<LatLng> points = m_polyline.getPoints();
        int list_size = points.size();
        Log.d("TrackModel", "Points size :" + Integer.toString(list_size));
        if(list_size > 1) {
            LatLng p1 = points.get(list_size - 2);
            LatLng p2 = points.get(list_size - 1);
            float bearing = (float)SphericalUtil.computeHeading(p1, p2);
            Log.d("TrackModel", "angle rotation : " + Float.toString(bearing));
            tractor.setBearing(bearing);
            return bearing;
        }
        return 0.0f;
    }
}
