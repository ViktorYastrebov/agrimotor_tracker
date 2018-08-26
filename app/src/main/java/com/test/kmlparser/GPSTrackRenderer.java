package com.test.kmlparser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.test.kmlparser.algo.GPSTrackObservable;
import com.test.kmlparser.algo.Observer;

public class GPSTrackRenderer implements Observer {

    private GoogleMap m_map;
    private int m_color;
    private Polyline m_polyline;

    private GPSTrackObservable m_subject;

    public GPSTrackRenderer(GoogleMap map, int color, GPSTrackObservable sub) {
        m_map = map;
        m_color = color;
        m_subject = sub;

        PolylineOptions po = new PolylineOptions().color(m_color).width(2.0f);
        po.addAll(m_subject.getPoints());
        m_polyline = m_map.addPolyline(po);
    }

    public void update() {
        m_polyline.setPoints(m_subject.getPoints());
    }
}
