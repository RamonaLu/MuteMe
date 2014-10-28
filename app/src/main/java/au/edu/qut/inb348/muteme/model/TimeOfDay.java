package au.edu.qut.inb348.muteme.model;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeOfDay {
	public int hour;
	public int minutes;
	public TimeOfDay(int hour, int minutes){
		this.hour = hour;
		this.minutes = minutes;
	}

    @Override
    public String toString(){
        return hour+":"+minutes;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof TimeOfDay)){
            return false;
        }
        else {
            TimeOfDay otherTime = (TimeOfDay) other;
            return otherTime.hour == hour && otherTime.minutes == minutes;
        }
    }
}