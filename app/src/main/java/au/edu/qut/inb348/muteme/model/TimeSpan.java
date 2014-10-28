package au.edu.qut.inb348.muteme.model;

public class TimeSpan {
	public static final TimeSpan ALL_DAY = new TimeSpan(new TimeOfDay(0,0), new TimeOfDay(0,0));

	public TimeOfDay start;
    public TimeOfDay stop;
	public TimeSpan(TimeOfDay start, TimeOfDay stop){
		this.start = start;
		this.stop = stop;
	}

    @Override
    public String toString(){
        return start.toString() + " - " + stop.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof TimeSpan)){
            return false;
        }
        else {
            TimeSpan otherSpan = (TimeSpan) other;
            return otherSpan.start.equals(start) && otherSpan.stop.equals(stop);
        }
    }
}
