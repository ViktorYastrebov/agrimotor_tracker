package com.test.kmlparser;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackModel {

    private Context m_owner = null;
    private GoogleMap m_map = null;

    private PolylineOptions m_polylineOptions = null;
    private Polyline m_polyline = null;
    private GroundOverlay tractor = null;
    private int iconId;

    //What's wrong. Why does Android have not Point2D impl ????
    private class Point2d {
        private double x;
        private double y;
        public Point2d(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
        double getX() { return x;}
        double getY() { return y;}
    }

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
                    .position(start_p, 10));
            m_polylineOptions.add(start_p);
            m_polyline = m_map.addPolyline(m_polylineOptions);
        }
    }

    private BitmapDescriptor getIconFromFile() {
        Bitmap bitmap= BitmapFactory.decodeResource(m_owner.getResources(), iconId);
        //R.drawable.track_final_2);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

    private float calcAngle(Point2d p1 , Point2d p2 ) {
        double ret = Math.acos(
                (p1.getX()*p2.getX() + p1.getY()*p2.getY()) /
                        ( Math.sqrt(Math.pow(p1.getX(), 2.0) + Math.pow(p1.getY(), 2.0))
                                * Math.sqrt(Math.pow(p2.getX(),2.0) + Math.pow(p2.getY(), 2.0))
                        )
        );
        return (float)ret;
    }

    private LatLng entityNorthVector(final LatLng p) {
        LatLng res = new LatLng(p.latitude + 0.0001, p.longitude);
        return res;
    }

    public void moveTo(LatLng p) {
        //find a way to optimize it. Something like mMap redraw current m_polyline
        m_polylineOptions.add(p);
        m_polyline.setPoints(m_polylineOptions.getPoints());
        tractor.setPosition(p);
        rotateIcon();
    }

    private void rotateIcon() {
        List<LatLng> points = m_polyline.getPoints();
        int list_size = points.size();
        if( list_size == 2) {
            LatLng first = points.get(0);
            LatLng second = points.get(1);
            Point2d vec1 = new Point2d(0.001, 0.0);
            Point2d vec2 = new Point2d( second.latitude - first.latitude,
                                        second.longitude - second.longitude);
            float angle = calcAngle(vec1, vec2);
            Log.d("TrackModel", "rotateIcon, angle :" + Float.toString(angle));
            tractor.setBearing(angle);

        } else if(list_size > 2) {
            LatLng p1 = points.get(list_size -3);
            LatLng p2 = points.get(list_size - 2);
            LatLng p3 = points.get(list_size - 1);

            Point2d v1 = new Point2d(p2.latitude - p1.latitude, p2.longitude - p1.longitude);
            Point2d v2 = new Point2d( p3.latitude - p2.latitude, p3.longitude - p2.longitude);
            float angle = calcAngle(v1, v2);
            //lastAngleRotation
            Log.d("TrackModel", "rotateIcon, angle :" + Float.toString(angle));
            tractor.setBearing(angle); //does it apply new value depends on north or current angle
        }
    }
}
