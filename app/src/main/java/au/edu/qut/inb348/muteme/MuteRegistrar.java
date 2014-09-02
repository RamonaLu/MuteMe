package au.edu.qut.inb348.muteme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import au.edu.qut.inb348.muteme.model.ChronoCondition;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.GeoCondition;
import au.edu.qut.inb348.muteme.model.Mute;
import au.edu.qut.inb348.muteme.model.TimeOfDay;
import au.edu.qut.inb348.muteme.model.TimeSpan;

public class MuteRegistrar  {
    LocationManager locationManager;
    AlarmManager alarmManager;
    Context context;

    public MuteRegistrar(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;

    }

    public void register(Mute m) {
        Intent muteCheck = new Intent(context, MuteCheckReceiver.class);
        muteCheck.putExtra(MuteCheckReceiver.MUTE_ID, m.id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)m.id, muteCheck, PendingIntent.FLAG_UPDATE_CURRENT);
        listenForChrono(m.chronoCondition, pendingIntent);
        if (m.geoCondition.radiusMetres != -1) {
            listenForGeo(m.geoCondition, pendingIntent);
        }
        context.sendBroadcast(muteCheck);
    }

    private void listenForGeo(GeoCondition geo, PendingIntent intent) {
        locationManager.addProximityAlert(geo.latitude, geo.longitude, geo.radiusMetres, -1, intent);
    }

    private void listenForChrono(ChronoCondition chrono, PendingIntent applyMute) {
        if (chrono != null) {
            TimeSpan timeSpan = chrono.timeSpan;
            for(DayOfWeek dayOfWeek : chrono.daysOfWeek){
                schedule(applyMute, dayOfWeek, timeSpan.start);
                schedule(applyMute, dayOfWeek, timeSpan.stop);
            }
        }

    }
    private void schedule(PendingIntent intent, DayOfWeek dayOfWeek,
                          TimeOfDay time) {
        long triggerMilliseconds = dayOfWeek.getMillisecondsUntil(time);
        long millisecondsWeekly = 7 * 24 * 60 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerMilliseconds, AlarmManager.INTERVAL_DAY * 7, intent);
    }

    public void deregister(Mute m) {
        Intent muteCheck = new Intent(context, MuteCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)m.id, muteCheck, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        locationManager.removeProximityAlert(pendingIntent);
    }
}
