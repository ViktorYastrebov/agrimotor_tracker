package com.test.kmlparser;


import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.model.LatLng;
import com.test.kmlparser.GRPMC.*;

public class DataReceiver extends TimerTask {

    //nmeaServerSocket.getInputStream();
    private final String files[] = {
        "D5_1_GPS-2018-04-22.txt",
        "M_1_GPS-2018-04-22.txt",
        "M_2_GPS-2018-04-22.txt",
        "M_3_GPS-2018-04-22.txt"
    };
    private TrackModel m_track_model;
    private GPRMCFileParser m_file_emul;
    private Timer m_timer;
    private boolean first = false;

    public DataReceiver(TrackModel trackModel, Timer timer) {
        m_track_model = trackModel;
        m_file_emul = new GPRMCFileParser("tracks/" + files[1], m_track_model.getContext());
        m_timer = timer;
    }

    public void run() {
        LatLng p = m_file_emul.process();
        if(p != null) {
            if(!first) {
                m_track_model.initialize(p);
                first = true;
            } else {
                m_track_model.moveTo(p);
            }
        } else {
            m_timer.cancel();
            this.cancel();
        }
    }
}
