package com.test.kmlparser.old_test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class TestCoordsParser {

    private List<LatLng> coords;
    public TestCoordsParser(String path, Context ctx) {
        try {
            coords = new ArrayList<LatLng>();
            InputStream inStream = ctx.getAssets().open(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String str;
            while ((str = in.readLine()) != null) {
                Log.d("Debug", " TestCoordsParser, str : " + str);
                List<String> strCoords = Arrays.asList(str.split(","));

                if(!strCoords.isEmpty()  && strCoords.size() >= 2) {
                    LatLng lng = new LatLng(Double.parseDouble(strCoords.get(0)), Double.parseDouble(strCoords.get(1)));
                    coords.add(lng);
                }
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<LatLng> getCoords() {
        return coords;
    }
}
