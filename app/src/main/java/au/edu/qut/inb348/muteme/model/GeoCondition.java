package au.edu.qut.inb348.muteme.model;

import android.location.Location;
import android.os.SystemClock;

public class GeoCondition {

    public static final GeoCondition EVERYWHERE = new GeoCondition(-1, -1, 100);

	public double longitude;
    public double latitude;
    public int radiusMetres;

	public GeoCondition(double latitude, double longitude, int radiusMetres)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.radiusMetres = radiusMetres;
	}

    public static int LOCATION_EXPIRY_MILLIS = 1000 * 60;

    public boolean isActivatedBy(Location location) {
        if (location == null) return false;

        long deltaMillis = location.getElapsedRealtimeNanos() - SystemClock.elapsedRealtimeNanos();
        if (deltaMillis < LOCATION_EXPIRY_MILLIS){
            float[] distanceCalcResult = new float[2];
            Location.distanceBetween(
                    location.getLatitude(),
                    location.getLongitude(),
                    latitude,
                    longitude,
                    distanceCalcResult);
            float deltaMetres = distanceCalcResult[0];
            return deltaMetres < radiusMetres;
        }
        else {
            return false;
        }


    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof GeoCondition)){
            return false;
        }
        else {
            GeoCondition otherGeo = (GeoCondition) other;
            return otherGeo.radiusMetres == radiusMetres && otherGeo.latitude == latitude && otherGeo.longitude == longitude;
        }
    }
}
