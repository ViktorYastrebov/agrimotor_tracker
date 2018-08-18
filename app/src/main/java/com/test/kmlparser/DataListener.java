package com.test.kmlparser;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.test.kmlparser.GRPMC.*;

import android.os.Handler;

public class DataListener implements Runnable {

    //private Context m_contex;
    private Handler m_handler;
    private GPRMCFileParser m_parser;
    private TrackModel m_track_model;
    private final long timerPause = 100; //ms

    private int initCameraPoints = 30;

    public DataListener(Handler main_handler, GPRMCFileParser parser, TrackModel trackModel) {
        this.m_handler = main_handler;
        this.m_parser = parser;
        this.m_track_model = trackModel;
    }

    @Override
    public void run() {

        // Delta around 3-5 meters. On curve - make it more detailed
        try {
            LatLng p = m_parser.process(); //data
            if(p != null) {
                Thread.sleep(timerPause); //emulation of read()
                //m_handler.postDelayed( new InitPoster(m_track_model,p), 2000);
                m_handler.post(new InitPoster(m_track_model,p));
            }

            p = m_parser.process();
            while( p != null) {
                Thread.sleep(timerPause);
                if(initCameraPoints != 0) {
                    //m_handler.postDelayed( new MessagePoster(m_track_model, p), 2000);
                    m_handler.post(new MessagePoster(m_track_model, p, true));
                    --initCameraPoints;
                } else {
                    m_handler.post(new MessagePoster(m_track_model, p, false));
                }
                p = m_parser.process();
            }
        } catch (Exception ex) {}
    }
}
