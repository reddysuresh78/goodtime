package core;

import java.util.Calendar;

/**
 * Created by Suresh on 23-10-2015.
 */
public class StarRange {

    private int starIndex;
    private Calendar startTime;
    private Calendar endTime;

    public StarRange( int starIndex, Calendar startTime, Calendar endTime) {
        this.starIndex = starIndex;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public int getStarIndex() {
        return starIndex;
    }

    public void setStarIndex(int starIndex) {
        this.starIndex = starIndex;
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
        return "StarRange{" +
                "starIndex=" + starIndex +
                ", startTime=" + startTime.getTime() +
                ", endTime=" + endTime.getTime() +
                '}';
    }
}
