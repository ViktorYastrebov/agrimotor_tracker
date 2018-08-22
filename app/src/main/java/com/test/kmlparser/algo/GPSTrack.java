package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import com.test.kmlparser.Observable;
import com.test.kmlparser.Observer;

import java.util.List;
import java.util.ArrayList;

public class GPSTrack  implements Observable {

    protected List<LatLng> m_points;
    protected Double m_scale;

    private List<Observer> m_obs;

    public GPSTrack(double scale) {
        m_points = new ArrayList<>();
        m_scale = new Double(scale);

        m_obs = new ArrayList<>();
    }

    public List<LatLng> getPoints() {
        return m_points;
    }

    public void add(LatLng p) {
        m_points.add(p);
        notifyObserver();
    }

    public Double getScale() {
        return m_scale;
    }

    public void addObserver(Observer o) {
        m_obs.add(o);
    }

    public void removeObserver(Observer o) {
        m_obs.add(o);
    }

    public void notifyObserver() {
        for(Observer o : m_obs) {
            o.update(this);
        }
    }
}
