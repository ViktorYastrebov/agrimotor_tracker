package com.test.kmlparser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.test.kmlparser.algo.GPSTrack;

public class DrawableGPSTrack extends GPSTrack {

    private GoogleMap m_map;
    private int m_color;
    private Polyline m_polyline;

    public DrawableGPSTrack(double scale, GoogleMap map, int color) {
        super(scale);
        m_map = map;
        m_color = color;
    }

    public void initialize(LatLng p) {
        super.initialize(p);
        PolylineOptions po = new PolylineOptions().color(m_color).add(p).width(2.0f); //not quite perfect
        m_polyline = m_map.addPolyline(po);
    }

    public void add(LatLng p) {
        super.add(p);
        m_polyline.setPoints(m_points);
    }
}
