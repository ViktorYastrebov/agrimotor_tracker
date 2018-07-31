package com.test.kmlparser.RDP;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class Line {
    
    private LatLng start;
    private LatLng end;
    
    private double dx;
    private double dy;
    private double sxey;
    private double exsy;
    private  double length;
    
    public Line(LatLng start, LatLng end) {
        this.start = start;
        this.end = end;
        dx = start.latitude - end.latitude;
        dy = start.longitude - end.longitude;
        sxey = start.latitude * end.longitude;
        exsy = end.latitude * start.longitude;
        length = Math.sqrt(dx*dx + dy*dy);
    }
    
    @SuppressWarnings("unchecked")
    public List<LatLng> asList() {
        return Arrays.asList(start, end);
    }
    
    public double distance(LatLng p) {
        return Math.abs(dy * p.latitude - dx * p.longitude + sxey - exsy) / length;
    }
}


