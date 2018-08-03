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

    /*
       function rotatePolygon(polygon,angle) {
        var map = polygon.getMap();
        var prj = map.getProjection();
        var origin = prj.fromLatLngToPoint(polygon.getPath().getAt(0)); //rotate around first point

        var coords = polygon.getPath().getArray().map(function(latLng){
           var point = prj.fromLatLngToPoint(latLng);
           var rotatedLatLng =  prj.fromPointToLatLng(rotatePoint(point,origin,angle));
           return {lat: rotatedLatLng.lat(), lng: rotatedLatLng.lng()};
        });
        polygon.setPath(coords);
    }

    function rotatePoint(point, origin, angle) {
        var angleRad = angle * Math.PI / 180.0;
        return {
            x: Math.cos(angleRad) * (point.x - origin.x) - Math.sin(angleRad) * (point.y - origin.y) + origin.x,
            y: Math.sin(angleRad) * (point.x - origin.x) + Math.cos(angleRad) * (point.y - origin.y) + origin.y
        };
    }
     */
    /*
    private LatLng getRotatedLat(LatLng point, float angle, LatLng aroundPoint) {
        double angleRad = angle * Math.PI / 180;
        double x = Math.cos(angleRad) * (point.latitude - aroundPoint.latitude) - Math.sin(angleRad) * (point.longitude - aroundPoint.longitude) + aroundPoint.latitude;
        double y = Math.sin(angleRad) * (point.latitude - aroundPoint.latitude) + Math.cos(angleRad) * (point.longitude - aroundPoint.longitude) + aroundPoint.longitude;
        return  new LatLng(x, y);
    } */


    private LatLng getRotatedLat(double x, double y, float angle, LatLng aroundPoint) {
        double angleRad = angle * Math.PI / 180;
        double x1 = Math.cos(angleRad) * (x - aroundPoint.latitude) - Math.sin(angleRad) * (x - aroundPoint.longitude) + aroundPoint.latitude;
        double y1 = Math.sin(angleRad) * (y - aroundPoint.latitude) + Math.cos(angleRad) * (y - aroundPoint.longitude) + aroundPoint.longitude;
        Log.d("getRotatedLat", "x :" + Double.toString(x1) + ", y :" + Double.toString(y));
        return  new LatLng(x1, y1);
    }

     private void moveBoundBox(float angle) {

         LatLngBounds bounds = tractor.getBounds();
         //TODO: Memory usage issue
         List<LatLng> lst = new ArrayList<LatLng>();

         /*
         LatLng centerPoint = bounds.getCenter();
         lst.add(getRotatedLat(bounds.northeast.latitude, bounds.northeast.longitude, angle, centerPoint));
         lst.add(getRotatedLat(bounds.southwest.latitude, bounds.northeast.longitude, angle, centerPoint));
         lst.add(getRotatedLat(bounds.southwest.latitude, bounds.southwest.longitude, angle, centerPoint));
         lst.add(getRotatedLat(bounds.northeast.latitude, bounds.southwest.longitude, angle, centerPoint));
            */

         /*
         LatLng centerPoint = bounds.getCenter();
         LatLng p1 = getRotatedLat(bounds.northeast.latitude, bounds.northeast.longitude, angle, centerPoint);
         LatLng p2 = getRotatedLat(bounds.southwest.latitude, bounds.northeast.longitude, angle, centerPoint);
         LatLng p3 = getRotatedLat(bounds.southwest.latitude, bounds.southwest.longitude, angle, centerPoint);
         LatLng p4 = getRotatedLat(bounds.northeast.latitude, bounds.southwest.longitude, angle, centerPoint);
         */


         LatLng p1 = new LatLng(bounds.northeast.latitude, bounds.northeast.longitude);
         LatLng p2 = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
         LatLng p3 = new LatLng(bounds.southwest.latitude, bounds.southwest.longitude);
         LatLng p4 = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);


         m_map.addCircle(new CircleOptions().center(p1).radius(1.0).fillColor(Color.rgb(255,215,0)));  //gold
         m_map.addCircle(new CircleOptions().center(p2).radius(1.0).fillColor(Color.rgb(220,20,60))); //red
         m_map.addCircle(new CircleOptions().center(p3).radius(1.0).fillColor(Color.rgb(0,128,0)));  //green
         m_map.addCircle(new CircleOptions().center(p4).radius(1.0).fillColor(Color.rgb(0,0,128)));  //blue

         lst.add(p1); lst.add(p2); lst.add(p3); lst.add(p4);
         /*
         lst.add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude));
         lst.add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude));
         lst.add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude));
         lst.add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude));
          */
         boundBox.setPoints(lst);

         //drawGridBy(bounds.southwest, bounds.northeast);
     }

    private BitmapDescriptor getIconFromFile() {
        Bitmap bitmap= BitmapFactory.decodeResource(m_owner.getResources(), iconId);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

    public void moveTo(LatLng p) {
        //find a way to optimize it. Something like mMap redraw current m_polyline
        m_polylineOptions.add(p);
        m_polyline.setPoints(m_polylineOptions.getPoints());
        tractor.setPosition(p);

        //https://stackoverflow.com/questions/35977204/how-to-draw-a-polygon-rectangle-on-google-maps-with-defined-distance-meters
        float angle = rotateIcon();
        moveBoundBox(angle);
    }

    private void drawGridBy(/*LatLng p1, LatLng p2,*/ LatLng lineP1, LatLng lineP2) {
        int seedersCount = 5;
        double length = SphericalUtil.computeDistanceBetween(lineP1, lineP2);
        Log.d("drawGridBy", "length :" + Double.toString(length));

        double seederSizeDelta = length / seedersCount;

        {
            LatLng offset = SphericalUtil.computeOffset(lineP1, seederSizeDelta, 0.0);
            m_map.addCircle(new CircleOptions().center(offset).radius(1.0).fillColor(Color.GREEN));

            for(int i=0; i < seedersCount -1 ; ++i) {
                offset = SphericalUtil.computeOffset(offset, seederSizeDelta, 0.0);
                m_map.addCircle(new CircleOptions().center(offset).radius(1.0).fillColor(Color.GREEN));
            }
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
