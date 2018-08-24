package com.test.kmlparser;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.test.kmlparser.algo.GPSTrack;

import android.util.Log;

public class DrawableGPSTrack extends GPSTrack implements  Observable {

    private GoogleMap m_map;
    private int m_color;
    private Polyline m_polyline;

    private List<Observer> m_observers;

    public DrawableGPSTrack(double scale, GoogleMap map, int color) {
        super(scale);
        m_map = map;
        m_color = color;
        m_observers = new ArrayList<Observer>();
    }

    public void initialize(LatLng p) {
        super.initialize(p);
        PolylineOptions po = new PolylineOptions().color(m_color).add(p).width(2.0f); //not quite perfect
        m_polyline = m_map.addPolyline(po);
    }

    public void add(LatLng p) {
        super.add(p);
        m_polyline.setPoints(m_points);
        Log.d("DrawableGPSTrack", "add calls");
        notifyObservers();
    }

    public void addObserver(Observer o) {
        m_observers.add(o);
    }
    public void removeObserver(Observer o) {
        m_observers.remove(o);
    }
    public void notifyObservers() {
        for(Observer o : m_observers) {
            o.update();
        }
    }


}
