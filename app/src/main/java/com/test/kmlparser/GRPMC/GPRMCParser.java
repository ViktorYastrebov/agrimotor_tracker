package com.test.kmlparser.GRPMC;

import com.google.android.gms.maps.model.LatLng;

//import java.time.LocalTime;
//import java.time.*;

public class GPRMCParser {

    // $GPRMC - Recommended Minimum Specific GPS/TRANSIT Data 
    // Format 1: 
    // $GPRMC,025423.494,A,3709.0642,N,14207.8315,W,7.094,108.52,200505,13.1,E*12, 
    // $GPRMC,    1     ,2,    3    ,4,     5    ,6,  7  ,  8   ,  9   ,  A ,B*MM,E 
    //      1   UTC time of position HHMMSS 
    //      2   validity of the fix ("A" = valid, "V" = invalid) 
    //      3   current latitude in ddmm.mm format 
    //      4   latitude hemisphere ("N" = northern, "S" = southern) 
    //      5   current longitude in dddmm.mm format 
    //      6   longitude hemisphere ("E" = eastern, "W" = western) 
    //      7   speed in knots 
    //      8   true course in degrees 
    //      9   date in DDMMYY format 
    //      A   magnetic variation in degrees 
    //      B   direction of magnetic variation ("E" = east, "W" = west) 
    //      MM  checksum 
    //      E   extra data (may not be present) 
    // Format 2: 
    // $GPRMC,025423.494,A,3709.0642,N,14207.8315,W,7.094,108.52,200505,13.1,E,A*71 
    // $GPRMC,    1     ,2,    3    ,4,     5    ,6,  7  ,  8   ,  9   ,  A ,B,C*MM,E 
    //      C   Mode indicator, (A=Autonomous, D=Diff, E=Est, N=Not valid) 
	
	static private final String GPRMC_MARKER = "$GPRMC";
	
	//make it java.util.Optional
	//private LocalTime time;
	//private LocalDate date;
	
	private boolean validityOfFix;
	
	private double speedInKnots;
	private double trueCourse;
	
	private double latitude;
	private double longitude;
	
	private double parseDouble(String value, double def) {
		double ret = def;
		try {
			ret = Double.parseDouble(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	private double parseLatitude(String s, String d) {
		double _lat = parseDouble(s, 99999.0);
        if (_lat < 99999.0) { 
            double lat = (double)((long)_lat / 100L); // _lat is always positive here 
            lat += (_lat - (lat * 100.0)) / 60.0; 
            return d.equalsIgnoreCase("S")? -lat : lat; 
        } else { 
            return 90.0; // invalid latitude 
        }
	}
	
	
	private double parseLongitute(String s, String d) {
        double _lon = parseDouble(s, 99999.0); 
        if (_lon < 99999.0) { 
            double lon = (double)((long)_lon / 100L); // _lon is always positive here 
            lon += (_lon - (lon * 100.0)) / 60.0; 
            return d.equalsIgnoreCase("W")? -lon : lon; 
        } else { 
            return 180.0; // invalid longitude 
        } 
	}
	
	public boolean parse(String line) {
		if(line == null) {
			return false;
		}

		if(!line.startsWith(GPRMC_MARKER)) {
			return false;
		}

		String parts[] = line.split(",");
		

		if(parts.length < 9) {
			return false;
		}
		
		//must be datetime
		String hhmmss_ms  = parts[1];
		String dtStr = hhmmss_ms.substring(0, 2) + ":";
		dtStr +=  hhmmss_ms.substring(2,4) + ":";
		dtStr +=  hhmmss_ms.substring(4);
		
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").parse;
		System.out.println("Time " + dtStr);
		
		boolean equA = parts[2].equals("A");
		boolean equV = parts[2].equals("V");
		boolean isEmpty = parts[2].isEmpty(); 
		
		if( !equA && !equV && !isEmpty) {
			return false;
		}
		
		validityOfFix = equA && !equV; 
		
		latitude = parseLatitude(parts[3], parts[4]);
		longitude = parseLongitute(parts[5], parts[6]);
		
		speedInKnots = parseDouble(parts[7], 0.0);
		trueCourse = parseDouble(parts[8], 0.0);
		
		String dateStr = parts[9].substring(0, 2) + "/" + parts[9].substring(2, 4) + "/" + parts[9].substring(4);
		System.out.println("Date " + dateStr);
		// currently ignore the A ,B*MM,E 
		// or A ,B,C*MM,E 
		return true;
	}
	
	
	public double getLat() {
		return latitude;
	}
	
	public double getLng() {
		return longitude;
	}

	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
	}

}
