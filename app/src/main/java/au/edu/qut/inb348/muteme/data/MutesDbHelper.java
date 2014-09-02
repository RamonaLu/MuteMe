package au.edu.qut.inb348.muteme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import au.edu.qut.inb348.muteme.model.ChronoCondition;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.GeoCondition;
import au.edu.qut.inb348.muteme.model.Mute;
import au.edu.qut.inb348.muteme.model.TimeOfDay;
import au.edu.qut.inb348.muteme.model.TimeSpan;

public class MutesDbHelper extends SQLiteOpenHelper {

    public MutesDbHelper(Context context){
        super(context, MutesContract.DATABASE_NAME, null, MutesContract.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MutesContract.Mutes.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  MutesContract.Mutes.TABLE_NAME);
        onCreate(db);
    }

    public void addMute(Mute mute) {
        ContentValues values = GetAsContentValues(mute);

        SQLiteDatabase db = getWritableDatabase();

        mute.id = db.insert(MutesContract.Mutes.TABLE_NAME, null, values);
        db.close();
    }

    private ContentValues GetAsContentValues(Mute mute) {
        ContentValues values = new ContentValues();
        values.put(MutesContract.Mutes._TITLE, mute.title);
        values.put(MutesContract.Mutes._CHRONO_START_HOUR, mute.chronoCondition.timeSpan.start.hour);
        values.put(MutesContract.Mutes._CHRONO_START_MINUTES, mute.chronoCondition.timeSpan.start.minutes);
        values.put(MutesContract.Mutes._CHRONO_STOP_HOUR, mute.chronoCondition.timeSpan.stop.hour);
        values.put(MutesContract.Mutes._CHRONO_STOP_MINUTES, mute.chronoCondition.timeSpan.stop.minutes);
        values.put(MutesContract.Mutes._CHRONO_MONDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.MONDAY));
        values.put(MutesContract.Mutes._CHRONO_TUESDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.TUESDAY));
        values.put(MutesContract.Mutes._CHRONO_WEDNESDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.WEDNESDAY));
        values.put(MutesContract.Mutes._CHRONO_THURSDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.THURSDAY));
        values.put(MutesContract.Mutes._CHRONO_FRIDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.FRIDAY));
        values.put(MutesContract.Mutes._CHRONO_SATURDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.SATURDAY));
        values.put(MutesContract.Mutes._CHRONO_SUNDAY, mute.chronoCondition.daysOfWeek.contains(DayOfWeek.SUNDAY));
        values.put(MutesContract.Mutes._GEO_LATITUDE, mute.geoCondition.latitude);
        values.put(MutesContract.Mutes._GEO_LONG, mute.geoCondition.longitude);
        values.put(MutesContract.Mutes._GEO_RADIUS_METRES, mute.geoCondition.radiusMetres);
        return values;
    }

    public boolean deleteMute(long id) {
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(MutesContract.Mutes.TABLE_NAME, MutesContract.Mutes._ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
        return result > 0;
    }
    public List<Mute> getMutes() {
        List<Mute> mutes = new ArrayList<Mute>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MutesContract.Mutes.TABLE_NAME, MutesContract.Mutes.PROJECTION, null, null, null , null, null);
        while(cursor.moveToNext()) {
            Mute mute = ConstructMute(cursor);
            mutes.add(mute);
        }
        cursor.close();
        db.close();
        return mutes;
    }

    public Mute getMute(long id) {
        String query = MutesContract.Mutes.sqlSelect(id);

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Mute mute = null;

        if (cursor.moveToFirst()){
            mute = ConstructMute(cursor);
            cursor.close();
        }
        db.close();

        return mute;
    }
    public void updateMute(Mute mute) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = GetAsContentValues(mute);
        db.update(MutesContract.Mutes.TABLE_NAME, values, MutesContract.Mutes._ID+" = "+mute.id, null);
        db.close();
    }

    private Mute ConstructMute(Cursor cursor) {
        long  id = cursor.getLong(cursor.getColumnIndex(MutesContract.Mutes._ID));
        TimeOfDay start = new TimeOfDay(
        cursor.getInt(cursor.getColumnIndex(MutesContract.Mutes._CHRONO_START_HOUR)),
        cursor.getInt(cursor.getColumnIndex(MutesContract.Mutes._CHRONO_START_MINUTES)));
        TimeOfDay stop = new TimeOfDay(
                cursor.getInt(cursor.getColumnIndex(MutesContract.Mutes._CHRONO_STOP_HOUR)),
                cursor.getInt(cursor.getColumnIndex(MutesContract.Mutes._CHRONO_STOP_MINUTES)));
        TimeSpan timeSpan = new TimeSpan(start,stop);
        EnumSet<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);
        addConditionally(DayOfWeek.MONDAY, daysOfWeek, MutesContract.Mutes._CHRONO_MONDAY, cursor);
        addConditionally(DayOfWeek.TUESDAY, daysOfWeek, MutesContract.Mutes._CHRONO_TUESDAY, cursor);
        addConditionally(DayOfWeek.WEDNESDAY, daysOfWeek, MutesContract.Mutes._CHRONO_WEDNESDAY, cursor);
        addConditionally(DayOfWeek.THURSDAY, daysOfWeek, MutesContract.Mutes._CHRONO_THURSDAY, cursor);
        addConditionally(DayOfWeek.FRIDAY, daysOfWeek, MutesContract.Mutes._CHRONO_FRIDAY, cursor);
        addConditionally(DayOfWeek.SATURDAY, daysOfWeek, MutesContract.Mutes._CHRONO_SATURDAY, cursor);
        addConditionally(DayOfWeek.SUNDAY, daysOfWeek, MutesContract.Mutes._CHRONO_SUNDAY, cursor);
        ChronoCondition chrono = new ChronoCondition(timeSpan, daysOfWeek);

        GeoCondition geo = new GeoCondition(
                cursor.getDouble(cursor.getColumnIndex(MutesContract.Mutes._GEO_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(MutesContract.Mutes._GEO_LONG)),
                cursor.getInt(cursor.getColumnIndex(MutesContract.Mutes._GEO_RADIUS_METRES)));
        String title = cursor.getString(cursor.getColumnIndex(MutesContract.Mutes._TITLE));
        return new Mute(id, geo, chrono,title);
    }

    private void addConditionally(DayOfWeek day, EnumSet<DayOfWeek> daysOfWeek, String columnName, Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(columnName)) == 1) {
            daysOfWeek.add(day);
        }
    }


}
