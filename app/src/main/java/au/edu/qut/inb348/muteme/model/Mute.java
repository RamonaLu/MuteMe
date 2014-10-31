package au.edu.qut.inb348.muteme.model;

/*
    Written by Chong Lu.
 */
public class Mute {
    public long id;
	public GeoCondition geoCondition;
    public ChronoCondition chronoCondition;
    public String title;

	public Mute(long id, GeoCondition geoCondition, ChronoCondition chronoCondition, String title){
        this.id = id;
		this.geoCondition = geoCondition;
		this.chronoCondition = chronoCondition;
        this.title = title;
	}

    public Mute() {
        this(-1, GeoCondition.EVERYWHERE, ChronoCondition.ALWAYS, "Everywhere");
    }


}
