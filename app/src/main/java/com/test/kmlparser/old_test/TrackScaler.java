package com.test.kmlparser.old_test;

import java.util.List;
import com.google.android.gms.maps.model.LatLng;
import com.test.kmlparser.RDP.SeriesReducer;

public class TrackScaler implements Visitor {

    //https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
    //https://github.com/LukaszWiktor/series-reducer

    private List<LatLng> scaledPoints;
    private Double scaleValue;

    public TrackScaler(Double scale) {
        super();
        scaleValue = new Double(scale);
    }

    public void visit(Tracker tracker) {
        scaledPoints = SeriesReducer.reduce(tracker.getPoints(), scaleValue.doubleValue());
    }

    public List<LatLng> getScaledPoints() {
        return scaledPoints;
    }
}
