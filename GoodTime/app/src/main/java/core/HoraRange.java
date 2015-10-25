package core;

import java.util.Calendar;

/**
 * Created by Suresh on 23-10-2015.
 */
public class HoraRange {

    private int planet;
    private Calendar startTime;
    private Calendar endTime;

    public HoraRange(int planet, Calendar startTime, Calendar endTime) {
        this.planet = planet;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getPlanet() {
        return planet;
    }

    public void setPlanet(int planet) {
        this.planet = planet;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "HoraRange{" +
                "planet='" + planet + '\'' +
                ", startTime=" + (startTime == null ? " " :startTime.getTime()) +
                ", endTime=" + (endTime == null ? " " : endTime.getTime()) +
                '}';
    }
}
