package au.edu.qut.inb348.muteme.model;

/*
    Written by Chong Lu.
 */
public class TimeOfDay {
	public int hour;
	public int minutes;
	public TimeOfDay(int hour, int minutes){
		this.hour = hour;
		this.minutes = minutes;
	}

    @Override
    public String toString(){
        return String.format("%d:%02d", hour, minutes);
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