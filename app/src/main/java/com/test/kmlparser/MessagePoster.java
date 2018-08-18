package com.test.kmlparser;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MessagePoster implements Runnable {

    private TrackModel m_track_model;
    private  LatLng m_point;

    private boolean forceCamera;

    public MessagePoster(TrackModel model, LatLng p, boolean forceCamera) {
        this.m_track_model = model;
        this.m_point = p;
        this.forceCamera = forceCamera;
    }

    @Override
    public void run() {
        m_track_model.moveTo(m_point);
        if(forceCamera) {
            m_track_model.TargetCamera(m_point);
        }
        Log.d("MessagePoster", "moveTo(" +
                Double.toString(m_point.latitude) + "," + Double.toString(m_point.longitude) + ")");
    }
}
