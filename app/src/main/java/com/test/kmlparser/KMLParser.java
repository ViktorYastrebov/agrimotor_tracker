package com.test.kmlparser;

import android.util.Log;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;


public class KMLParser {

    ArrayList<PolygonOptions> polygons;
    static private final String splitMarker = ";";

    KMLParser(String file, Context ctx)
    {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream inStream =  ctx.getAssets().open(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String str;
            while ((str = in.readLine()) != null) {
                str+= splitMarker; //needs for splitting
                buf.append(str);
            }
            in.close();
            String inputFileContents = buf.toString();

            Document doc = Jsoup.parse(inputFileContents, "", Parser.xmlParser());
            polygons = new ArrayList<PolygonOptions>();
            for(Element p : doc.select("Polygon")) {
                PolygonOptions pl = new PolygonOptions();
                for(Element e: p.select("coordinates")) {
                    String[] lines = e.text().split(splitMarker);

                    for (int i = 0; i < lines.length; ++i) {
                        if(!lines[i].isEmpty()) {
                            String[] points = lines[i].split(",");
                            if(points.length > 2) {
                                Log.d("KmlParser", "p1 : " + points[0] + ", p2 :" + points[1]);
                                //example of file entry : 30.2835506,50.2704952,0
                                // first is Y, X for LatLng ctor
                                double x = Double.parseDouble(points[1]);
                                double y = Double.parseDouble(points[0]);
                                pl.add(new LatLng(x, y));
                            }
                        }
                    }
                }
                polygons.add(pl);
            }
        } catch(Exception e) {
            Log.d("KMLParser", "ctor :" + e.getMessage());
        }
    }

    public ArrayList<PolygonOptions> getData() {
        return  polygons;
    }
}
