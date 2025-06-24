package org.hbrs.se2.project.startupx.util;

public class BewertungenUtil {
    private BewertungenUtil() {}

    public static String createAverageStars(Double average) {
        if(average != 0){
            return String.format("%.1f ⭐", average);
        } else {
            return "Keine Bewertungen";
        }
    }

}
