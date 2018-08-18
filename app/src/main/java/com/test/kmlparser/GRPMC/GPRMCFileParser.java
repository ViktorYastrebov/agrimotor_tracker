package com.test.kmlparser.GRPMC;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

import java.io.*;

public class GPRMCFileParser {
	private BufferedReader m_read_buffer = null;
    private GPRMCParser m_parser = null;

    public GPRMCFileParser(String path, Context ctx) {
        try {
            InputStream inStream = ctx.getAssets().open(path);
            m_read_buffer = new BufferedReader(new InputStreamReader(inStream));

            m_parser = new GPRMCParser();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

	public LatLng process() {
        try {
            if ((m_read_buffer != null)) {
                String line = m_read_buffer.readLine();
                if(line != null) {
                    //ignore not parsed lines
                    while(!m_parser.parse(line)) {
                        line = m_read_buffer.readLine();
                        if(line == null) {
                            return null;
                        }
                    }
                    return m_parser.getLatLng();
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            Log.d("GPRMCFileParser", "process , exception : " + ex.getMessage());
            return null;
        }
        return null;
    }
}
