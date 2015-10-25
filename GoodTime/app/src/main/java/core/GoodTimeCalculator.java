package core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import swisseph.DblObj;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;
import swisseph.TCPlanet;
import swisseph.TransitCalculator;

public class GoodTimeCalculator {

    public static InfoHolder calculateMoon(double longitude, double latitude, int numDays){

        InfoHolder info = new InfoHolder();

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, numDays);

        long timeZoneOffset = TimeZone.getDefault().getRawOffset();

        double moonLong = 0.0D;
        double sunLong = 0.0D;

        int jday = cal.get(Calendar.DAY_OF_MONTH);  //5
        int jmon = (cal.get(Calendar.MONTH) + 1); //2
        int jyear = cal.get(Calendar.YEAR); //1
//        double thour = (cal.get(11) + cal.get(12) / 60.0D + cal.get(13) / 3600.0D);

        double jut = 0.0D;
        double top_elev = 0.0D;
        double tjd = 2415020.5D;
        double[] x = new double[6];

        int sidMode = 1;//LAHIRI
        StringBuffer serr = new StringBuffer();
        boolean gregflag = true;
        int iflag = 0;

        iflag &= -8;
        iflag |= 4;  // use Moshier ephemeris
        iflag |= 65536;
        iflag |= 256;

        SwissEph sw = new SwissEph();
        SweDate sd =  new SweDate(tjd, gregflag);

        sw.swe_set_ephe_path("");

        sw.swe_set_topo(longitude, latitude, top_elev);
        sw.swe_set_sid_mode(sidMode, 0.0D, 0.0D);


        jut = 0.0D;
        sd.setDate(jyear, jmon, jday, jut);
        sd.setCalendarType(gregflag, true);
        tjd = sd.getJulDay();

        tjd += 0 / 24.0D;
        sd.setJulDay(tjd);

        //Adjust timezone offset
//        tjd = tjd - (timeZoneOffset/(1000*60*60));
//        sd.setJulDay(tjd);

        double delt = SweDate.getDeltaT(tjd);
        double te = tjd + delt;

        sw.swe_calc(te, 0, iflag, x, serr);
        sunLong = x[0];

        // for moon the second argument should be 1.
        sw.swe_calc(te, 1, iflag, x, serr);
        moonLong = x[0];

        double offsetHrs = timeZoneOffset/(1000*60*60.0d);

        DblObj returnedTime = new DblObj();
        //Determine sun rise time
        sw.swe_rise_trans(te, 0, null, 4, 1, new double[]{longitude, latitude, top_elev}, 0, 0, returnedTime, serr);

        info.setSunriseTime(toDate(returnedTime.val, offsetHrs));

//        System.out.print("\nsunrise time" + info.getSunriseTime().getTime());

        //Determine sunset time
        returnedTime = new DblObj();
        sw.swe_rise_trans(te, 0, null, 4, 2, new double[]{longitude, latitude, top_elev}, 0, 0, returnedTime, serr);
        info.setSunsetTime(toDate(returnedTime.val, offsetHrs));

        info.setHoras(calcHoras(info.getSunriseTime(), info.getSunsetTime()));

//        System.out.print("\nsunset time" + toDate(returnedTime.val, offsetHrs).getTime());

        int nakshatra = ((int) Math.floor(moonLong / 13.333333333333334D));

        double adjustedMoonLong = moonLong >= sunLong ? moonLong : moonLong + 360.0D;

        int tithi = ((int) Math.floor((adjustedMoonLong - sunLong) / 12.0D) + 1);

        if (tithi <= 15) {
            tithi = tithi;
        } else if (tithi < 30) {
            tithi = (tithi - 15)  ;
        } else {
            tithi = 0;
        }

        info.setThidhi(tithi);

        nakshatra++;

        double nakshStart = getNextNakshatraStart(sw, sd.getJulDay(), nakshatra, longitude, latitude);
        double nakshEnd = getNextNakshatraEnd(sw, sd.getJulDay(), nakshatra, longitude, latitude);

        StarRange todayRange = new StarRange( nakshatra , toDate(nakshStart, offsetHrs),  toDate(nakshEnd, offsetHrs));

//        System.out.println("\ntodayRange" + todayRange);

        nakshStart = getNextNakshatraStart(sw, sd.getJulDay(), (nakshatra +1)%27  , longitude, latitude);
        nakshEnd = getNextNakshatraEnd(sw, sd.getJulDay(), (nakshatra + 1) % 27, longitude, latitude);

        StarRange tomorrowRange = new StarRange( (nakshatra +1)%27 , toDate(nakshStart, offsetHrs),  toDate(nakshEnd, offsetHrs));

//        System.out.println("\ntomorrowRange" + tomorrowRange);
        //We may have to tweat this
        tjd = sd.getJulDay();
        tjd -= 24/24;
        sd.setJulDay(tjd);

        nakshStart = getNextNakshatraStart(sw, sd.getJulDay(), (nakshatra -1)%27 , longitude, latitude );
        nakshEnd = getNextNakshatraEnd(sw, sd.getJulDay(), (nakshatra -1)%27 , longitude, latitude);

        StarRange yesterDayRange = new StarRange( (nakshatra -1)%27  , toDate(nakshStart, offsetHrs),  toDate(nakshEnd, offsetHrs));

//        System.out.println("\nyesterDayRange" + yesterDayRange);

        if(tomorrowRange.getStartTime().get(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH)){
            info.setFirstStar(todayRange);
            info.setSecondStar(tomorrowRange );
        }else if(yesterDayRange.getEndTime().get(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH)){
            info.setFirstStar(yesterDayRange);
            info.setSecondStar(todayRange);
        }

        System.out.println("\n" + info);

        return info;

     }

    private static Calendar toDate(double d, double tzHours) {
        SweDate sd = new SweDate(d + tzHours / 24. + 0.5/24./3600. /* round to second */);
        double hour = sd.getHour();

        Calendar c = Calendar.getInstance();
        c.set(sd.getYear(), sd.getMonth() - 1, sd.getDay(), (int) hour, (int) ((hour - (int) hour) * 60), (int) ((hour * 60 - (int) (hour * 60)) * 60));
        return c;
    }

    private static List<HoraRange> calcHoras(Calendar sunRiseTime, Calendar sunSetTime)  {

        long diff = Math.round((sunSetTime.getTime().getTime() - sunRiseTime.getTime().getTime()) / (12));

        Calendar sunRiseTimeLocal = Calendar.getInstance();
        sunRiseTimeLocal.setTime(sunRiseTime.getTime());

        Calendar[] horas = new Calendar[24];
        for(int i=0;i<24;i++){
            Calendar c = Calendar.getInstance();
            c.setTime(sunRiseTimeLocal.getTime());
            horas[i] = c;
            sunRiseTimeLocal.setTime(new Date(sunRiseTimeLocal.getTime().getTime()  + diff));
         }
        horas[23] = sunRiseTimeLocal;

        int curDay = sunRiseTime.get(Calendar.DAY_OF_WEEK);

        List<HoraRange> horaRanges = new ArrayList<HoraRange>();
        for(int i=0; i<horas.length;i++){
            horaRanges.add(new HoraRange(curDay+i, horas[i], i+1 == horas.length ? null: horas[i+1]));
        }

        return horaRanges;
    }

    private static double getNextNakshatraStart(SwissEph sw, double juld, int nakshatra,double longitude, double latitude  ) {
        double geopos[] = new double[] {longitude, latitude, 0};

        sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI, 0, 0);
        sw.swe_set_topo(geopos[0], geopos[1], 0);
        int flags = SweConst.SEFLG_MOSEPH|
                SweConst.SEFLG_TRANSIT_LONGITUDE |
                SweConst.SEFLG_SIDEREAL;

        TransitCalculator tc = new TCPlanet(
                sw,
                SweConst.SE_MOON,
                flags,
                nakshatra * (360. / 27.));

        return sw.getTransitUT(tc, juld, false);
    }
    private static double getNextNakshatraEnd(SwissEph sw, double juld, int nakshatra,double longitude, double latitude) {
        return getNextNakshatraStart(sw, juld, (nakshatra + 1) % 27, longitude, latitude);
    }

    public static void main(String[] args) throws Exception {
        calculateMoon(78.45636, 17.38405,1);



    }


}
