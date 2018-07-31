package com.test.kmlparser.RDP;

import com.google.android.gms.maps.model.LatLng;
import com.test.kmlparser.RDP.Line;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SeriesReducer {

    /**
     * Reduces number of points in given series using Ramer-Douglas-Peucker algorithm.
     * 
     * @param points
     *          initial, ordered list of points (objects implementing the {@link Point} interface)
     * @param epsilon
     *          allowed margin of the resulting curve, has to be > 0
     */
    public static List<LatLng> reduce(List<LatLng> points, double epsilon) {
        if (epsilon < 0) {
            throw new IllegalArgumentException("Epsilon cannot be less then 0.");
        }
        double furthestPointDistance = 0.0;
        int furthestPointIndex = 0;

        Log.d("Debug","points size :" + Integer.toString(points.size()) );

        Line line = new Line(points.get(0), points.get(points.size() - 1));
        for (int i = 1; i < points.size() - 1; i++) {
            double distance = line.distance(points.get(i));
            if (distance > furthestPointDistance ) {
                furthestPointDistance = distance;
                furthestPointIndex = i;
            }
        }
        if (furthestPointDistance > epsilon) {
            List<LatLng> reduced1 = reduce(points.subList(0, furthestPointIndex+1), epsilon);
            List<LatLng> reduced2 = reduce(points.subList(furthestPointIndex, points.size()), epsilon);
            List<LatLng> result = new ArrayList<LatLng>(reduced1);
            result.addAll(reduced2.subList(1, reduced2.size()));
            return result;
        } else {
            return line.asList();
        }
    }
}
