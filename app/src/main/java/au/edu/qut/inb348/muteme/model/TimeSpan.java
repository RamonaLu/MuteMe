package au.edu.qut.inb348.muteme.model;

public class TimeSpan {
	public static final TimeSpan ALL_DAY = new TimeSpan(new TimeOfDay(0,0), new TimeOfDay(0,0));

	public TimeOfDay start;
    public TimeOfDay stop;
	public TimeSpan(TimeOfDay start, TimeOfDay stop){
		this.start = start;
		this.stop = stop;
	}

}
