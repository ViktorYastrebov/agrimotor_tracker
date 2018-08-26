package com.test.kmlparser.algo;

import java.util.ArrayList;
import java.util.List;

public class GPSScaledTrackObservable extends GPSScaledTrack implements Observable {

    private List<Observer> m_observers;
    public GPSScaledTrackObservable(double triangleScale, double distanceScale) {
        super(triangleScale, distanceScale);
        m_observers = new ArrayList<>();
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
