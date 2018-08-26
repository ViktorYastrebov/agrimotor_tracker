package com.test.kmlparser.algo;

public class GPSScaledTrack extends GPSTrack {
    protected Double m_tri_scale;
    protected Double m_dist_scale;

    public GPSScaledTrack(double triangleScale, double distanceScale) {
        super();
        m_tri_scale = new Double(triangleScale);
        m_dist_scale  = new Double(distanceScale);
    }

    public Double getTriScale() {
        return m_tri_scale;
    }

    public Double getDistScale() {
        return m_dist_scale;
    }
}
