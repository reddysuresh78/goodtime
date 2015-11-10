package core;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Suresh on 23-10-2015.
 */
public class InfoHolder {

    private StarRange firstStar;
    private StarRange secondStar;

    private Calendar sunriseTime;
    private Calendar sunsetTime;

    private int horaTime;

    private int thidhi;

    List<HoraRange> horas = new ArrayList<HoraRange>(24);

    public int getHoraTime() {
        return horaTime;
    }

    public void setHoraTime(int horaTime) {
        this.horaTime = horaTime;
    }

    public int getThidhi() {
        return thidhi;
    }

    public void setThidhi(int thidhi) {
        this.thidhi = thidhi;
    }

    public Calendar getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Calendar sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public Calendar getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Calendar sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public StarRange getFirstStar() {
        return firstStar;
    }

    public void setFirstStar(StarRange firstStar) {
        this.firstStar = firstStar;
    }

    public List<HoraRange> getHoras() {
        return horas;
    }

    public void setHoras(List<HoraRange> horas) {
        this.horas = horas;
    }

    public StarRange getSecondStar() {
        return secondStar;
    }

    public void setSecondStar(StarRange secondStar) {
        this.secondStar = secondStar;
    }

    @Override
    public String toString() {
        return "InfoHolder{" +
                "firstStar=" + firstStar +
                ", secondStar=" + secondStar +
                ", sunriseTime=" + sunriseTime.getTime()  +
                ", sunsetTime=" + sunsetTime.getTime()  +
                ", thidhi=" + thidhi +
                ", horas=" + horas +
                '}';
    }
}
