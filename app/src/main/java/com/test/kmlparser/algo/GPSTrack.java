package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import java.util.ArrayList;

public class GPSTrack implements GPSTrackBase{

    protected List<LatLng> m_points;

    public GPSTrack() {
        m_points = new ArrayList<>();
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

    public int size() {
        return m_points.size();
    }

}
