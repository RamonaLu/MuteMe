package au.edu.qut.inb348.muteme.model;


import java.util.EnumSet;



public class ChronoCondition {
    public static final ChronoCondition ALWAYS = new ChronoCondition(TimeSpan.ALL_DAY, EnumSet.allOf(DayOfWeek.class));

    public EnumSet<DayOfWeek> daysOfWeek;

	public TimeSpan timeSpan;
	
	public ChronoCondition(TimeSpan timeSpan, EnumSet<DayOfWeek> daysOfWeek) {

		this.timeSpan = timeSpan;
		this.daysOfWeek = daysOfWeek;
	}


    @Override
    public String toString() {
        StringBuilder daysOfWeekBuilder = new StringBuilder();
        for(DayOfWeek d : daysOfWeek) {
            daysOfWeekBuilder.append(d.name().charAt(0));
        }
        return timeSpan.toString() + " " + daysOfWeekBuilder.toString();
    }
}
