package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.ArrayList;

public class GPSTrackObservable extends GPSTrack implements Observable {

    private List<Observer> m_observers;

    public GPSTrackObservable() {
        super();
        m_observers = new ArrayList<>();
    }

    public void add(LatLng p) {
        super.add(p);
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
