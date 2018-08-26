package com.test.kmlparser;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;

import com.google.android.gms.maps.model.PolygonOptions;

import com.test.kmlparser.algo.GPSPathScaler;
import com.test.kmlparser.algo.GPSScaledTrack;
import com.test.kmlparser.algo.GPSTrackObservable;

public class TrackModel {

    private Context m_owner = null;
    private GoogleMap m_map = null;
    private GroundOverlay tractor = null;
    private int iconId;

    private List<GPSScaledTrack> m_track_list;
    private GPSPathScaler m_track_scaler;
    private DrawableGPSTrack m_track;

    SeederAttr m_seederAttr;

    private Polygon boundBox = null;

    public TrackModel(GoogleMap m, Context ctx, int iconId, int sowerCount, int sowerLength)
    {
        m_owner = ctx;
        m_map = m;
        this.iconId = iconId;
        m_seederAttr = new SeederAttr(sowerCount, sowerLength);

        m_track_list = new ArrayList<>();
        //temporary test
        m_track = new DrawableGPSTrack( 0.01, 50.0, m_map, Color.GREEN);
        m_track_list.add( m_track );
        //m_track_list.add( new DrawableGPSTrack(4,40.0, m_map, Color.BLUE));
        //m_track_list.add( new DrawableGPSTrack(2, 20.0, m_map, Color.RED));

        //m_track_list.add( new DrawableGPSTrack( 50.0, m_map, Color.GREEN) );
        m_track_scaler = new GPSPathScaler(m_track_list);
    }

    public void initialize(LatLng start_p) {
        tractor = m_map.addGroundOverlay(new GroundOverlayOptions()
                .image(getIconFromFile())
                .position(start_p, 10, 10)
                .anchor(0.5f, 0.5f)
        );

        m_track_scaler.initialize(start_p);
        m_track.addObserver( new SeededGridDrawer(m_track, m_map, m_seederAttr));

        GPSTrackObservable baseTrack = m_track_scaler.getBaseTrack();
        baseTrack.addObserver(new GPSTrackRenderer(m_map, Color.GRAY, baseTrack));

        m_map.moveCamera(CameraUpdateFactory.newLatLng(start_p));
        m_map.animateCamera(CameraUpdateFactory.zoomTo(19.0f));
    }

    public void moveTo(LatLng p) {
        // May be THERE IS A HARD WAY TO JUST UPDATE THE CURRENT points + Force to redraw !!!!
        //From documentation:
        // To alter the shape of the polyline after
        // it has been added, you can call Polyline.setPoints()
        // and provide a new list of points for the polyline.
        double bearing = m_track_scaler.process(p);
        tractor.setPosition(p);
        tractor.setBearing((float)bearing);
    }

    public Context getContext() {
        return m_owner;
    }

    private void init_bounds() {
        LatLngBounds bounds = tractor.getBounds();
        final PolygonOptions polygonOptions = new PolygonOptions()
                .add(new LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.northeast.longitude))
                .add(new LatLng(bounds.southwest.latitude, bounds.southwest.longitude))
                .add(new LatLng(bounds.northeast.latitude, bounds.southwest.longitude))
                .strokeColor(Color.GREEN);
        boundBox = m_map.addPolygon(polygonOptions);
    }

    /*
    private void updateBounds() {

        LatLngBounds bounds = tractor.getBounds();
        //TODO: Memory usage issue
        List<LatLng> lst = new ArrayList<LatLng>();

        LatLng p1 = new LatLng(bounds.northeast.latitude, bounds.northeast.longitude);
        LatLng p2 = new LatLng(bounds.southwest.latitude, bounds.northeast.longitude);
        LatLng p3 = new LatLng(bounds.southwest.latitude, bounds.southwest.longitude);
        LatLng p4 = new LatLng(bounds.northeast.latitude, bounds.southwest.longitude);
        lst.add(p1); lst.add(p2); lst.add(p3); lst.add(p4);
        boundBox.setPoints(lst);
    } */

    private BitmapDescriptor getIconFromFile() {
        Bitmap bitmap= BitmapFactory.decodeResource(m_owner.getResources(), iconId);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

    public void TargetCamera(LatLng p) {
        m_map.moveCamera(CameraUpdateFactory.newLatLng(p));
        m_map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
    /*public void visualTrace(LatLng p) {
        m_map.addMarker(new MarkerOptions().position(p));
    } */
}
