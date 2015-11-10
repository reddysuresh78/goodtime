package core;

import java.util.Calendar;

/**
 * Created by Suresh on 25-10-2015.
 */
public class Utils {

    private static String GOOD_PLANETS = "0,1,2,3,5";
    private static String BAD_PLANETS = "4,6";

    private static int[] RULING_PLANET_INDEX = {1,4,7,3,6,2,5};

    public static boolean isGoodPlanet(int planet){

        if(GOOD_PLANETS.contains(Integer.toString(planet))){
            return true;
        }
        return false;
    }

    public static int getRulingPlanetIndexForDay(int day){
        return RULING_PLANET_INDEX[day];
    }

    public static boolean isGoodTime(InfoHolder infoHolder){

        Calendar cal = Calendar.getInstance();

        for(HoraRange horaRange : infoHolder.getHoras()){

            if( (horaRange.getStartTime().before(cal) || horaRange.getStartTime().equals(cal)) &&
                    (horaRange.getEndTime().after(cal) || horaRange.getEndTime().equals(cal))) {
                 return(isGoodPlanet(horaRange.getPlanet()));
            }
        }
        return false;
    }

    public static int getCurrentStar(InfoHolder infoHolder){

        Calendar cal = Calendar.getInstance();

        StarRange curRange = infoHolder.getFirstStar();



        if( (curRange.getStartTime().before(cal) || curRange.getStartTime().equals(cal)) &&
                (curRange.getEndTime().after(cal) || curRange.getEndTime().equals(cal))) {
            return curRange.getStarIndex();
        }else{
            return infoHolder.getSecondStar().getStarIndex();
        }

    }


    public static int getCurrentHora(InfoHolder infoHolder) {
        Calendar cal = Calendar.getInstance();

        int i=0;
        for(HoraRange horaRange : infoHolder.getHoras()){

            if( (horaRange.getStartTime().before(cal) || horaRange.getStartTime().equals(cal)) &&
                    (horaRange.getEndTime().after(cal) || horaRange.getEndTime().equals(cal))) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public static boolean isGoodStar(String[] mStarsArray, int curStarIndex, String nativeStar) {

        int nativeStarIndex = 0;
        for(int i=0;i<mStarsArray.length;i++){
            if(mStarsArray[i].equalsIgnoreCase(nativeStar)){
                nativeStarIndex = i;
                break;
            }
        }

        int diff = 0;

        if(nativeStarIndex <= curStarIndex) {
            diff = ( curStarIndex -  nativeStarIndex + 1) %9;
        }else{
            diff = ( (27 -nativeStarIndex) +  curStarIndex + 1) %9;
        }

        if(diff == 2 || diff == 4 || diff == 6 || diff == 8 || diff ==0) {
            return true;
        }
        return false;
     }

    public static int getStartingAngle(Calendar sunTime) {

        int hours = sunTime.get(Calendar.HOUR) - 3;
        int minute = sunTime.get(Calendar.MINUTE);

        if(hours < 0)
            hours+=12;

        return hours*30 + (minute/2);

    }

    public static String[] getLabels(InfoHolder info) {

        String[] labels = new String[12];

        for(int i=0; i< 12; i++) {
            labels[i] =  info.getHoras().get(i).getDisplayString();
        }

        return labels;

    }
}
