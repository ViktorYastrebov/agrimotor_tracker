package com.test.kmlparser.algo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.util.Log;

public class GPSPathScaler {

    //private List<LatLng> m_all_points;
    private GPSTrackObservable m_all_points;
    protected Map<Double, GPSScaledTrack> m_scales;
    protected ArrayList<Double> m_keys;
    private int m_last_key;

    public GPSPathScaler(List<GPSScaledTrack> tracks) {
        m_scales = new TreeMap<>();
        for(GPSScaledTrack track : tracks) {
            m_scales.put(track.getTriScale(), track);
        }
        //m_all_points = new ArrayList<>();
        m_all_points = new GPSTrackObservable();
    }

    public void initialize(LatLng p) {
        m_all_points.add(p);
        for( Map.Entry<Double, GPSScaledTrack> elem  : m_scales.entrySet()) {
            elem.getValue().initialize(p);
        }
        m_keys = new ArrayList(m_scales.keySet());
        m_last_key = m_keys.size() - 1;
    }

    public GPSTrackObservable getBaseTrack() {
        return m_all_points;
    }

    public double process(LatLng p) {

        /*
        addHelper(scale2, p, 4.0, 8.0, scale3);
        addHelper(scale1, p, 2.0, 4.0, scale2);
        addHelper(points, p, 0.0, 2.0, scale1); */

        for(int i = m_last_key; i > 0; --i) {
            Double prev = m_keys.get(i - 1);
            Double cur = m_keys.get(i);
            GPSScaledTrack from = m_scales.get(prev);
            GPSScaledTrack to = m_scales.get(cur);

            int from_size = from.size();
            if(from_size > 1) {
                List<LatLng> triangle = new ArrayList<>();
                triangle.add(from.getPoints().get(from_size - 2));
                triangle.add(from.getPoints().get(from_size - 1));
                triangle.add(p);
                //List<LatLng> trinangle = from.getPoints().subList(from_size -2, from_size);
                //trinangle.add(p);
                double triangleSquare = SphericalUtil.computeArea(triangle);
                //LatLng p1 = from.getPoints().get( from_size - 2);
                //LatLng p2 = from.getPoints().get( from_size - 1);
                //double triangleSquare = calcTriangleSquare(p1, p2, p); // it's not in the Meters

                Log.d("before add", "triangle :" + Double.toString(triangleSquare) +
                        ",  scale :" + Double.toString(to.getTriScale()));
                if(triangleSquare > to.getTriScale()) {
                    to.add(p);
                } else {
                    LatLng prevTo = to.getPoints().get(to.size() - 1);
                    double dist = SphericalUtil.computeDistanceBetween(prevTo, p);
                    Log.d("before add 1", "dist :" + Double.toString(dist) +
                            ",  scale :" + Double.toString(to.getDistScale()));
                    if(dist >= to.getDistScale()) {
                        to.add(p);
                    }
                }
            }
        }

        Double prev = m_keys.get(0);
        GPSScaledTrack to  = m_scales.get(prev);
        int size = m_all_points.size();
        if(size > 1) {
            //List<LatLng> triangle = m_all_points.subList(size - 2, size);
            //triangle.add(p);

            List<LatLng> triangle = new ArrayList<>();
            triangle.add(m_all_points.getPoints().get(size - 2));
            triangle.add(m_all_points.getPoints().get(size - 1));
            triangle.add(p);

            double triSquare = SphericalUtil.computeArea(triangle);
            //LatLng p1 = m_all_points.get( size - 2);
            //LatLng p2 = m_all_points.get( size - 1);
            //double triSquare = calcTriangleSquare(p1, p2, p);
            Log.d("before add 2", "triangle :" + Double.toString(triSquare) +
                    ",  scale :" + Double.toString(to.getTriScale()));
            if(triSquare > to.getTriScale()) {
                to.add(p);
            } else {
                LatLng prevTo = to.getPoints().get(to.size() - 1);
                double dist = SphericalUtil.computeDistanceBetween(prevTo, p);
                Log.d("before add 3", "dist :" + Double.toString(dist) +
                        ",  scale :" + Double.toString(to.getDistScale()));
                if(dist >= to.getDistScale()) {
                    to.add(p);
                }
            }
        }
        LatLng prev_gen_track = m_all_points.getPoints().get(m_all_points.size() - 1);
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
