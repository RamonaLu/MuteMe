package au.edu.qut.inb348.muteme.model;
import java.util.Calendar;

/*
    Written by Chong Lu.
 */
public enum DayOfWeek{
	MONDAY(Calendar.MONDAY),
	TUESDAY(Calendar.TUESDAY),
	WEDNESDAY(Calendar.WEDNESDAY),
	THURSDAY(Calendar.THURSDAY),
	FRIDAY(Calendar.FRIDAY),
	SATURDAY(Calendar.SATURDAY),
	SUNDAY(Calendar.SUNDAY);
	
	private int value;

	private DayOfWeek(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

	public Long getMillisecondsUntil(TimeOfDay time) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, getValue());
		c.set(Calendar.HOUR_OF_DAY, time.hour);
		c.set(Calendar.MINUTE,time.minutes);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
		return c.getTimeInMillis();
	}
}
