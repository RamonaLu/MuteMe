package au.edu.qut.inb348.muteme.model;

public class GeoCondition {

    public static final GeoCondition EVERYWHERE = new GeoCondition(-1, -1, -1);

	public double longitude;
    public double latitude;
    public int radiusMetres;
	
	public static int DEFAULT_RADIUS = 50;
	public GeoCondition(double latitude, double longitude) {
		this(latitude, longitude, DEFAULT_RADIUS);
	}
	public GeoCondition(double latitude, double longitude, int radiusMetres)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.radiusMetres = radiusMetres;
	}

}
