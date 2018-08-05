package com.test.kmlparser;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

public class SownCell {

    private LatLng m_p1;
    private LatLng m_p2;
    private LatLng m_p3;
    private LatLng m_p4;

    SownCell(LatLng p1, LatLng p2, LatLng p3, LatLng p4)
    {
        m_p1 = p1; m_p2 = p2; m_p3 = p3; m_p4 = p4;
    }
    SownCell(List<LatLng> points) {
        if(points.size() > 3) {
            m_p1 = points.get(0);
            m_p2 = points.get(1);
            m_p3 = points.get(2);
            m_p4 = points.get(3);
        }
    }

    PolygonOptions getPolygon() {
        PolygonOptions po = new PolygonOptions();
        po.add(m_p1).add(m_p2).add(m_p3).add(m_p4);

        //temporary usage;
        //po.strokeWidth(2.0f);
        po.strokeColor(Color.rgb(255,215,0));
        return po;
    }
}
