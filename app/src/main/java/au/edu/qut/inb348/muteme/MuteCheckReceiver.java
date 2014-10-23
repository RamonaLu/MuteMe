package au.edu.qut.inb348.muteme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;

import java.util.Calendar;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.ChronoCondition;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.GeoCondition;
import au.edu.qut.inb348.muteme.model.Mute;

public class MuteCheckReceiver extends BroadcastReceiver{

    AudioManager audioManager;
    LocationManager locationManager;
    public static final String MUTE_ID="MUTE_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            MutesDbHelper dbHelper = new MutesDbHelper(context);
            long muteId = extras.getLong(MUTE_ID);

            Mute mute = dbHelper.getMute(muteId);



            mutePhone(isActive(mute.chronoCondition) && isActive(mute.geoCondition));
        }
    }
    private Location getMostRecentLastKnownLocation() {

        Location[] lastKnownLocations = new Location[] {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER),
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        };
        Location mostRecentLastKnownLocation = null;
        for(Location l : lastKnownLocations) {
            if (mostRecentLastKnownLocation == null ||
                    l.getElapsedRealtimeNanos() < mostRecentLastKnownLocation.getElapsedRealtimeNanos()){
                mostRecentLastKnownLocation = l;
            }
        }
        return mostRecentLastKnownLocation;
    }
    private boolean isActive(GeoCondition geo) {
        Location mostRecentLastKnownLocation = getMostRecentLastKnownLocation();
        return geo.isActivatedBy(mostRecentLastKnownLocation);
    }

    private boolean isActive(ChronoCondition chrono) {
        long nowMillis = Calendar.getInstance().getTimeInMillis();
        for(DayOfWeek dayOfWeek : chrono.daysOfWeek) {
            long startMillis = dayOfWeek.getMillisecondsUntil(chrono.timeSpan.start);
            long stopMillis = dayOfWeek.getMillisecondsUntil(chrono.timeSpan.stop);
            if (startMillis < nowMillis && nowMillis < stopMillis) {
              return true;
            }
        }
        return false;
    }

    private void mutePhone(boolean mute) {
        if (mute) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
        else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
