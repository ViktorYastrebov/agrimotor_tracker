package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface GPSTrackBase {
    public List<LatLng> getPoints();
    public void initialize(LatLng p);
    public void add(LatLng p);
    public int size();
}
