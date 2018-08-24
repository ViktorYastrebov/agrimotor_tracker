package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.ArrayList;

public class GPSTrack {

    protected List<LatLng> m_points;
    protected Double m_scale;

    public GPSTrack(double scale) {
        m_points = new ArrayList<>();
        m_scale = new Double(scale);
    }

    public List<LatLng> getPoints() {
        return m_points;
    }

    public void initialize(LatLng p) {
        m_points.add(p);
    }

    public void add(LatLng p) {
        m_points.add(p);
    }

    public Double getScale() {
        return m_scale;
    }
    public int size() {
        return m_points.size();
    }
}
