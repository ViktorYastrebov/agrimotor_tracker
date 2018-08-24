package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.util.Log;

public class GPSPathScaler {

    private List<LatLng> m_all_points;
    //protected Map<Double, List<LatLng> > m_scales; //should be ordered
    protected Map<Double, GPSTrack> m_scales;
    protected ArrayList<Double> m_keys;
    private int m_last_key;

    private ScaleStrategy m_scaleStr;

    private interface ScaleStrategy {

        //TODO: make general interface for condition of adding
        //TODO: example : 0 < x < N1 => List1, N1 < x < N2 => List2 etc. But it does not work correctly for the
        //TODO: Distance strategy 0 < x < N1 will aways gives true !!!

        void addHelper(GPSTrack from, GPSTrack to, LatLng p);
        void addHelper(List<LatLng> from, GPSTrack to, LatLng p);
    }

    private class TriangleStrategy implements ScaleStrategy {
        public void addHelper(GPSTrack from, GPSTrack to, LatLng p) {
            int size = from.size();
            if(size > 1) {
                List<LatLng> triangle = from.getPoints().subList(size - 2, size - 1);
                triangle.add(p);
                double s = SphericalUtil.computeArea(triangle);

                Log.d("addHelper", "triangle size :" + Double.toString(s));
                if(s > to.getScale()) {
                    to.add(p);
                }
            }
        }

        public void addHelper(List<LatLng> from, GPSTrack to, LatLng p) {
            int size = from.size();
            if(size > 1) {
                //this line only a difference !!! It  would be good to have an interface.
                // So there should not duplicate the code
                List<LatLng> triangle = from.subList(size - 2, size - 1);
                triangle.add(p);
                double s = SphericalUtil.computeArea(triangle);
                Log.d("addHelper", "triangle size :" + Double.toString(s));
                if(s > to.getScale()) {
                    to.add(p);
                }
            }
        }
    }

    public class DistanceStrategy implements ScaleStrategy {
        public void addHelper(GPSTrack from, GPSTrack to, LatLng p) {
            int size = from.size();
            LatLng prev = from.getPoints().get(size - 1);
            double dist = SphericalUtil.computeDistanceBetween(prev, p);

            Log.d("DistanceStrategy", "dist : " + Double.toString(dist));

            if(  dist > to.getScale() ) {
                to.add(p);
            }
        }

        public void addHelper(List<LatLng> from, GPSTrack to, LatLng p) {
            int size = from.size();
            LatLng prev = from.get(size - 1);
            double dist = SphericalUtil.computeDistanceBetween(prev, p);
            if( dist > to.getScale()) {
                to.add(p);
            }
        }
    }

    public GPSPathScaler( List<GPSTrack> tracks) {
        m_scales = new TreeMap<>();
        for(GPSTrack track : tracks) {
            m_scales.put(track.getScale(), track);
        }
        m_all_points = new ArrayList<>();
        m_scaleStr = new DistanceStrategy();
    }

    public void initialize(LatLng p) {
        m_all_points.add(p);
        for( Map.Entry<Double, GPSTrack> elem  : m_scales.entrySet()) {
            elem.getValue().initialize(p);
        }
        m_keys = new ArrayList(m_scales.keySet());
        m_last_key = m_keys.size() - 1;
    }

    public double process(LatLng p) {

        /*
        addHelper(scale2, p, 4.0, 8.0, scale3);
        addHelper(scale1, p, 2.0, 4.0, scale2);
        addHelper(points, p, 0.0, 2.0, scale1); */

        /*
        double key = m_keys.get(m_last_key).doubleValue();
        GPSTrack prevPoints = m_scales.get(key);

        for(int i = m_last_key - 1; i>= 0; --i) {
            double nextKey = m_keys.get(i).doubleValue();
            GPSTrack points = m_scales.get(nextKey);
            //addHelper(prevPoints, points, nextKey, key, p);
            m_scaleStr.addHelper(prevPoints,points,  p);
            key = nextKey;
            prevPoints = points;
        }
        m_scaleStr.addHelper(m_all_points, prevPoints, key, p);
        //addHelper(m_all_points, prevPoints, 0.0, key, p);

        LatLng prev = m_all_points.get(m_all_points.size() - 1);
        double bearing = SphericalUtil.computeHeading(prev, p);
        m_all_points.add(p);
        return bearing;
        */

        /*
        for(int i = m_last_key; i > 0; --i) {
            Double prev = m_keys.get(i - 1);
            Double cur = m_keys.get(i);
            GPSTrack from = m_scales.get(prev);
            GPSTrack to = m_scales.get(cur);
            m_scaleStr.addHelper(from, to, p);
        }
        Double prev = m_keys.get(0);
        GPSTrack to  = m_scales.get(prev);
        m_scaleStr.addHelper(m_all_points, to, p); */

        for(Map.Entry<Double, GPSTrack> e : m_scales.entrySet()) {
            GPSTrack track = e.getValue();
            LatLng prev = track.getPoints().get(track.size() - 1);
            double dist = SphericalUtil.computeDistanceBetween(prev, p);
            if(dist >= track.getScale()) {
                track.add(p);
            }
        }
        LatLng prev_gen_track = m_all_points.get(m_all_points.size() - 1);
        double bearing = SphericalUtil.computeHeading(prev_gen_track, p);
        m_all_points.add(p);
        return bearing;
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
