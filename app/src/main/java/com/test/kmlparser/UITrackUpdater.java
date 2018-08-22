package com.test.kmlparser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.test.kmlparser.algo.GPSTrack;

public class UITrackUpdater implements Observer {

    private Polyline m_polyline;
    UITrackUpdater(GoogleMap map, LatLng start, int color) {
        PolylineOptions po = new PolylineOptions().add(start).color(color);
        m_polyline = map.addPolyline(po);
    }

    public void update(Observable o) {
        GPSTrack track  = (GPSTrack)o;
        m_polyline.setPoints(track.getPoints());
    }
}
