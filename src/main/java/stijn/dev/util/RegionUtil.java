package stijn.dev.util;

import java.util.*;

public class RegionUtil {
    private static HashMap<String,String> regionMarkers = new HashMap<>();
    private static boolean hasBeenFilled = false;

    public HashMap<String,String> getRegionMarkers(){
        if(hasBeenFilled){
            fillRegionSignifiersList();
        }
        return regionMarkers;
    }


    private void fillRegionSignifiersList(){
        regionMarkers.put("J","Japan");
        regionMarkers.put("Japan","Japan");
        regionMarkers.put("USA","USA");
    }

    public static String getFileRegion(String filename) {
        List<String> segments = Arrays.stream(filename.split("\\(|\\)")).toList();
        String returnRegion ="";
        for (String region : segments) {
            region = region.toLowerCase().replace('(',' ').replace(')',' ').trim();
            //System.out.println("Trying segment: "+region);
            if (region.equals("j") || region.equals("japan")) {
                returnRegion = "Japan";
            } else if (region.equals("usa")) {
                returnRegion = "USA";
            }
        }
        if(returnRegion.isEmpty()||returnRegion==null){
            return "N/A";
        }
        return returnRegion;
    }
}
