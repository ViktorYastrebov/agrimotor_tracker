package com.test.kmlparser;

import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class InitPoster implements Runnable {
    private TrackModel m_track_model;
    private LatLng m_point;

    public InitPoster(TrackModel model, LatLng p) {
        this.m_track_model = model;
        this.m_point = p;
    }

    @Override
    public void run() {
        m_track_model.initialize(m_point);
        Log.d("InitPoster", "Initialized, point : " +
               Double.toString(m_point.latitude) + "," + Double.toString(m_point.longitude));
        //m_track_model.visualTrace(m_point);
    }
}
