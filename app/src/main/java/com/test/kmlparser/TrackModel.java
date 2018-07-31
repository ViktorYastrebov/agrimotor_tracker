package com.test.kmlparser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class TrackModel {

    private GoogleMap map;
    private PolylineOptions polylineOptions;

    public TrackModel(GoogleMap m)
    {
        map = m;
    }

    private float calcAngle(LatLng p1, LatLng p2) {
        double ret = Math.acos(
                (p1.latitude*p2.latitude + p1.longitude*p2.longitude) /
                        ( Math.sqrt(Math.pow(p1.latitude, 2.0) + Math.pow(p1.longitude, 2.0))
                                * Math.sqrt(Math.pow(p2.latitude,2.0) + Math.pow(p2.longitude, 2.0))
                        )
        );
        return (float)ret;
    }
}
