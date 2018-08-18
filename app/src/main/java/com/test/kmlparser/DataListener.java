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

    private int initCameraPoints = 3;

    public DataListener(Handler main_handler, GPRMCFileParser parser, TrackModel trackModel) {
        this.m_handler = main_handler;
        this.m_parser = parser;
        this.m_track_model = trackModel;
    }

    @Override
    public void run() {

        try {
            LatLng p = m_parser.process();
            if(p != null) {
                Thread.sleep(2000);
                //m_handler.postDelayed( new InitPoster(m_track_model,p), 2000);
                m_handler.post(new InitPoster(m_track_model,p));
            }

            p = m_parser.process();
            while( p != null) {
                Thread.sleep(2000);
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
