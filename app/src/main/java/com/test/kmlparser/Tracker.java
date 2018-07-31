package com.test.kmlparser;

import java.util.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class Tracker implements  Visitable {

    private PolygonOptions generalTrack;
    public Tracker() {
        generalTrack = new PolygonOptions();
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public void addPoint(LatLng point) {
            generalTrack.add(point);
    }
    public void addPoints(List<LatLng> lst) {
        generalTrack.addAll(lst);
    }

    public List<LatLng> getPoints() {
        return generalTrack.getPoints();
    }

    //can be cached
    public List<LatLng> getScaledPoints(Double scale) {
        TrackScaler scaler = new TrackScaler(scale);
        scaler.visit(this);
        return scaler.getScaledPoints();
    }
}
