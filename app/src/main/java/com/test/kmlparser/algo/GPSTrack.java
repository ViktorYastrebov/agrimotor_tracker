package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.ArrayList;

public class GPSTrack {

    protected List<LatLng> m_points;
    protected Double m_tri_scale;
    protected Double m_dist_scale;

    public GPSTrack(double triangleScale, double distanseScale) {
        m_points = new ArrayList<>();
        m_tri_scale = new Double(triangleScale);
        m_dist_scale  = new Double(distanseScale);
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

    public Double getTriScale() {
        return m_tri_scale;
    }

    public Double getDistScale() {
        return m_dist_scale;
    }

    public int size() {
        return m_points.size();
    }
}
