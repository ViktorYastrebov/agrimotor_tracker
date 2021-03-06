package com.test.kmlparser;


import java.nio.DoubleBuffer;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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

    SeederAttr m_seederAttr;

    private Polygon boundBox = null;

    public TrackModel(GoogleMap m, Context ctx, int iconId, int sowerCount, int sowerLength)
    {
        m_owner = ctx;
        m_map = m;
        m_polylineOptions = new PolylineOptions();
        m_polylineOptions.width(4.0f);
        this.iconId = iconId;

        m_seederAttr = new SeederAttr(sowerCount, sowerLength);
    }

    public void initialize(LatLng start_p) {

        //https://github.com/deepstreamIO/deepstream.io-client-java/issues/116
        // must be called from the main thread !!! ???

        if(tractor == null && m_polylineOptions.getPoints().isEmpty()) {
            tractor = m_map.addGroundOverlay(new GroundOverlayOptions()
                    .image(getIconFromFile())
                    .position(start_p, 10)
                    .anchor(0.5f, 0.5f)
            );

            tractor.setBearing(0);
            m_polylineOptions.add(start_p);
            m_polyline = m_map.addPolyline(m_polylineOptions);

            init_bounds();

            m_map.moveCamera(CameraUpdateFactory.newLatLng(start_p));
            m_map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        }
    }

    public void moveTo(LatLng p) {
        // TODO: Is there a way to just send redraw signal to GoogleMap it will optimize the memory usage
        // TODO: instead of m_polyline.setPoitns() use m_polylline.add() + signal

        /*Log.d("DataReceiver", "Moved To : " +
                Double.toString(p.latitude) + "," + Double.toString(p.longitude)); */

        //m_map.addCircle(new CircleOptions().strokeColor(Color.GREEN).radius(10.0));

        m_polylineOptions.add(p);
        List<LatLng> points =  m_polylineOptions.getPoints();
        m_polyline.setPoints(points);
        tractor.setPosition(p);
        updateBearing();
    }

    public Context getContext() {
        return m_owner;
    }

    private void init_bounds() {
        LatLngBounds bounds = tractor.getBounds();
        final PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                .strokeColor(Color.GREEN);
        boundBox = m_map.addPolygon(polygonOptions);
    }

    /*
    private void updateBounds() {

        LatLngBounds bounds = tractor.getBounds();
        //TODO: Memory usage issue
        List<LatLng> lst = new ArrayList<LatLng>();

        LatLng p1 = new LatLng(bounds.northeast.latitude, bounds.northeast.longitude);
        LatLng p2 = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
        LatLng p3 = new LatLng(bounds.southwest.latitude, bounds.southwest.longitude);
        LatLng p4 = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);
        lst.add(p1); lst.add(p2); lst.add(p3); lst.add(p4);
        boundBox.setPoints(lst);
    } */

    private BitmapDescriptor getIconFromFile() {
        Bitmap bitmap= BitmapFactory.decodeResource(m_owner.getResources(), iconId);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

    private void udpateSeedTrack(LatLng srcPoint, LatLng destPoint, float angle) {

        //TODO: optimize it to single for LOOP !!!. IT should not metter: even or odd !!!

        //List<PolygonOptions> polygons = new ArrayList<>();

        double sowerLength = m_seederAttr.getSowerLength();
        if(m_seederAttr.getSowerCount() % 2 == 1) {
            double startPoint = m_seederAttr.getSowerLength() / 2;
            int sowerCount = m_seederAttr.getSowerCount() / 2;

            LatLng src_l_p1 = SphericalUtil.computeOffset(srcPoint, startPoint, 90 + angle);
            LatLng dest_l_p1 = SphericalUtil.computeOffset(destPoint, startPoint, 90 + angle);

            LatLng src_r_p1 = SphericalUtil.computeOffset(srcPoint, startPoint, -90 + angle);
            LatLng dest_r_p1 = SphericalUtil.computeOffset(destPoint, startPoint, -90 + angle);

            SownCell middle = new SownCell(src_r_p1, dest_r_p1, dest_l_p1, src_l_p1);
            //polygons.add(middle.getPolygon());
            m_map.addPolygon(middle.getPolygon());

            for(int i = 0; i < sowerCount; ++i) {
                LatLng l_p2 = SphericalUtil.computeOffset(srcPoint, startPoint + (sowerLength * (i + 1)), 90 + angle );
                LatLng l_p1 = SphericalUtil.computeOffset(destPoint, startPoint + (sowerLength * (i + 1)), 90 + angle );

                SownCell sc_left = new SownCell(src_l_p1, dest_l_p1, l_p1, l_p2);
                m_map.addPolygon(sc_left.getPolygon());
                //polygons.add(sc_left.getPolygon());

                LatLng r_p2 = SphericalUtil.computeOffset(srcPoint, startPoint + (sowerLength * (i+1)), -90 + angle );
                LatLng r_p1 = SphericalUtil.computeOffset(destPoint, startPoint + (sowerLength * (i+1)), -90 + angle );

                SownCell sc_right = new SownCell(src_r_p1, dest_r_p1, r_p1, r_p2);
                m_map.addPolygon(sc_right.getPolygon());
                //polygons.add(sc_right.getPolygon());

                src_l_p1 = l_p2;
                dest_l_p1 = l_p1;
                src_r_p1 = r_p2;
                dest_r_p1 = r_p1;
            }
        } else {
            LatLng src_l_p1 = srcPoint;
            LatLng dest_l_p1 = destPoint;

            LatLng src_r_p1 = srcPoint;
            LatLng dest_r_p1 = destPoint;

            int sowerCount = m_seederAttr.getSowerCount() / 2;
            for(int i = 0; i < sowerCount; ++i) {
                LatLng l_p2 = SphericalUtil.computeOffset(srcPoint, (sowerLength * (i + 1)), 90 + angle );
                LatLng l_p1 = SphericalUtil.computeOffset(destPoint, (sowerLength * (i + 1)), 90 + angle );

                SownCell sc_left = new SownCell(src_l_p1, dest_l_p1, l_p1, l_p2);
                m_map.addPolygon(sc_left.getPolygon());
                //polygons.add(sc_left.getPolygon());

                LatLng r_p2 = SphericalUtil.computeOffset(srcPoint, (sowerLength * (i+1)), -90 + angle );
                LatLng r_p1 = SphericalUtil.computeOffset(destPoint, (sowerLength * (i+1)), -90 + angle );

                SownCell sc_right = new SownCell(src_r_p1, dest_r_p1, r_p1, r_p2);
                m_map.addPolygon(sc_right.getPolygon());
                //polygons.add(sc_right.getPolygon());

                src_l_p1 = l_p2;
                dest_l_p1 = l_p1;
                src_r_p1 = r_p2;
                dest_r_p1 = r_p1;
            }
        }

        /*
        for(PolygonOptions p : polygons) {
            m_map.addPolygon(p);
        } */
        tractor.setBearing(angle);
    }

    private void updateBearing() {
        List<LatLng> points = m_polyline.getPoints();
        int list_size = points.size();
        //Log.d("TrackModel", "Points size :" + Integer.toString(list_size));
        if(list_size > 1) {
            LatLng p1 = points.get(list_size - 2);
            LatLng p2 = points.get(list_size - 1);
            float bearing = (float)SphericalUtil.computeHeading(p1, p2);
            //Log.d("TrackModel", "angle rotation : " + Float.toString(bearing));
            //tractor.setBearing(bearing);
            udpateSeedTrack(p1, p2, bearing);
        }
    }

    public void TargetCamera(LatLng p) {
        m_map.moveCamera(CameraUpdateFactory.newLatLng(p));
        m_map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
    /*public void visualTrace(LatLng p) {
        m_map.addMarker(new MarkerOptions().position(p));
    } */
}
