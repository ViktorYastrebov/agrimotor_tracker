package com.test.kmlparser;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.test.kmlparser.algo.GPSTrack;
import com.test.kmlparser.algo.Observable;
import com.test.kmlparser.algo.Observer;

public class SeededGridDrawer implements Observer {
    private GoogleMap m_map;
    private GPSTrack m_subject;
    private SeederAttr m_attr;

    public SeededGridDrawer(Observable sub, GoogleMap map, SeederAttr attr) {
        m_map = map;
        m_subject = (GPSTrack) sub;
        m_attr = attr;
    }

    public void update() {

       int size = m_subject.size();
       if( size > 1) {
           LatLng srcPoint = m_subject.getPoints().get( size - 2);
           LatLng destPoint = m_subject.getPoints().get( size - 1);

           float angle = (float) SphericalUtil.computeHeading(srcPoint, destPoint);
           double sowerLength = m_attr.getSowerLength();
           int sowerCount = m_attr.getSowerCount() / 2;

           if(m_attr.getSowerCount() % 2 == 1) {
               double startPoint = m_attr.getSowerLength() / 2;

               LatLng src_l_p1 = SphericalUtil.computeOffset(srcPoint, startPoint, 90 + angle);
               LatLng dest_l_p1 = SphericalUtil.computeOffset(destPoint, startPoint, 90 + angle);

               LatLng src_r_p1 = SphericalUtil.computeOffset(srcPoint, startPoint, -90 + angle);
               LatLng dest_r_p1 = SphericalUtil.computeOffset(destPoint, startPoint, -90 + angle);

               SownCell middle = new SownCell(src_r_p1, dest_r_p1, dest_l_p1, src_l_p1);
               //polygons.add(middle.getPolygon());
               m_map.addPolygon(middle.getPolygon());

               for (int i = 0; i < sowerCount; ++i) {
                   LatLng l_p2 = SphericalUtil.computeOffset(srcPoint, startPoint + (sowerLength * (i + 1)), 90 + angle);
                   LatLng l_p1 = SphericalUtil.computeOffset(destPoint, startPoint + (sowerLength * (i + 1)), 90 + angle);

                   SownCell sc_left = new SownCell(src_l_p1, dest_l_p1, l_p1, l_p2);
                   m_map.addPolygon(sc_left.getPolygon());
                   //polygons.add(sc_left.getPolygon());

                   LatLng r_p2 = SphericalUtil.computeOffset(srcPoint, startPoint + (sowerLength * (i + 1)), -90 + angle);
                   LatLng r_p1 = SphericalUtil.computeOffset(destPoint, startPoint + (sowerLength * (i + 1)), -90 + angle);

                   SownCell sc_right = new SownCell(src_r_p1, dest_r_p1, r_p1, r_p2);
                   m_map.addPolygon(sc_right.getPolygon());
                   //polygons.add(sc_right.getPolygon());

                   src_l_p1 = l_p2;
                   dest_l_p1 = l_p1;
                   src_r_p1 = r_p2;
                   dest_r_p1 = r_p1;
               }
           } else {
               LatLng src_l_p1 = srcPoint;
               LatLng dest_l_p1 = destPoint;

               LatLng src_r_p1 = srcPoint;
               LatLng dest_r_p1 = destPoint;

               for (int i = 0; i < sowerCount; ++i) {
                   LatLng l_p2 = SphericalUtil.computeOffset(srcPoint, (sowerLength * (i + 1)), 90 + angle);
                   LatLng l_p1 = SphericalUtil.computeOffset(destPoint, (sowerLength * (i + 1)), 90 + angle);

                   SownCell sc_left = new SownCell(src_l_p1, dest_l_p1, l_p1, l_p2);
                   m_map.addPolygon(sc_left.getPolygon());
                   //polygons.add(sc_left.getPolygon());

                   LatLng r_p2 = SphericalUtil.computeOffset(srcPoint, (sowerLength * (i + 1)), -90 + angle);
                   LatLng r_p1 = SphericalUtil.computeOffset(destPoint, (sowerLength * (i + 1)), -90 + angle);

                   SownCell sc_right = new SownCell(src_r_p1, dest_r_p1, r_p1, r_p2);
                   m_map.addPolygon(sc_right.getPolygon());
                   //polygons.add(sc_right.getPolygon());

                   src_l_p1 = l_p2;
                   dest_l_p1 = l_p1;
                   src_r_p1 = r_p2;
                   dest_r_p1 = r_p1;
               }
           }
       }
    }
}
