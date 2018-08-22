package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GPSPathScaler {

    private List<LatLng> m_all_points;
    //protected Map<Double, List<LatLng> > m_scales; //should be ordered
    protected Map<Double, GPSTrack> m_scales;
    protected ArrayList<Double> m_keys;
    private int m_last_key;

    public GPSPathScaler() {
        m_scales = new TreeMap<>();
    }

    public GPSPathScaler( List<GPSTrack> tracks) {
        m_scales = new TreeMap<>();
        for(GPSTrack track : tracks) {
            m_scales.put(track.getScale(), track);
        }
    }

    public GPSTrack getScaled(double scale) {
        return m_scales.get(new Double(scale));
    }

    public void initialize(LatLng p) {
        m_all_points.add(p);
        for( Map.Entry<Double, GPSTrack> elem  : m_scales.entrySet()) {
            elem.getValue().add(p);
        }
        m_keys = new ArrayList(m_scales.keySet());
        m_last_key = m_keys.size() - 1;
    }

    public void process(LatLng p) {

        double key = m_keys.get(m_last_key).doubleValue();
        GPSTrack prevPoints = m_scales.get(key);

        for(int i = m_last_key - 1; i>= 0; --i) {
            double nextKey = m_keys.get(i).doubleValue();
            GPSTrack points = m_scales.get(nextKey);
            addHelper(prevPoints.getPoints(), points.getPoints(), nextKey, key, p);
            key = nextKey;
            prevPoints = points;
        }
        addHelper(m_all_points, prevPoints.getPoints(), 0.0, key, p);
        m_all_points.add(p);

        /*
        addHelper(scale2, p, 4.0, 8.0, scale3);
        addHelper(scale1, p, 2.0, 4.0, scale2);
        addHelper(points, p, 0.0, 2.0, scale1);

        points.add(p);
        */
    }

    private void addHelper(List<LatLng> from, List<LatLng> to, double minScale, double maxScale, LatLng p) {
        if(from.size() > 1) {
            LatLng p1 = from.get(from.size() - 2);
            LatLng p2 = from.get(from.size() - 1);
            double s = calcTriangleSquare(p1, p2, p);
            if(minScale < s && maxScale > s) {
                to.add(p);
            }
        }
    }

    private double calcTriangleSquare(LatLng p1, LatLng p2, LatLng p3) {
        /*double rt = 0.5 * Math.abs(
                ( (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) -
                        ( (p1.getY() - p3.getY()) * (p2.getX() - p3.getX())) )	);
        return new Double(rt); */
        double rt = 0.5 * Math.abs(( (p1.latitude - p3.latitude) * (p2.longitude - p3.longitude) -
                ( (p1.longitude - p3.longitude) * (p2.latitude - p3.latitude)) )	);
        return rt;
    }
}
