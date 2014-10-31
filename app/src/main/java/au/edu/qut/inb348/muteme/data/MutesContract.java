package au.edu.qut.inb348.muteme.data;

import android.provider.BaseColumns;

/*
    All the constants for working with the database (table and column names, etc) are defined in
    this class.

    Written by Chong Lu.
 */
public final class MutesContract {
    public static final String DATABASE_NAME = "muteme";
    public static final int DATABASE_VERSION = 3;


    public static final class Mutes implements BaseColumns {


        public static String sqlSelect(long id) {
            return "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " =  " + id ;
        }

        public static final String TABLE_NAME = "mute";
        public static final String _TITLE = "TITLE";
        public static final String _CHRONO_START_HOUR = "CHRONO_START_HOUR";
        public static final String _CHRONO_START_MINUTES = "CHRONO_START_MINUTES" ;
        public static final String _CHRONO_STOP_HOUR = "CHRONO_STOP_HOUR";
        public static final String _CHRONO_STOP_MINUTES = "CHRONO_STOP_MINUTES";
        public static final String _CHRONO_MONDAY = "CHRONO_MONDAY" ;
        public static final String _CHRONO_TUESDAY = "CHRONO_TUESDAY";
        public static final String _CHRONO_WEDNESDAY = "CHRONO_WEDNESDAY";
        public static final String _CHRONO_THURSDAY = "CHRONO_THURSDAY";
        public static final String _CHRONO_FRIDAY = "CHRONO_FRIDAY";
        public static final String _CHRONO_SATURDAY ="CHRONO_SATURDAY" ;
        public static final String _CHRONO_SUNDAY = "CHRONO_SUNDAY";
        public static final String _GEO_LATITUDE = "GEO_LATITUDE";
        public static final String _GEO_LONG = "GEO_LONG";
        public static final String _GEO_RADIUS_METRES = "GEO_RADIUS_METRES";
        public static final String[] PROJECTION = new String[] {
                _ID,
                _TITLE,
                _CHRONO_START_HOUR,
                _CHRONO_START_MINUTES,
                _CHRONO_STOP_HOUR,
                _CHRONO_STOP_MINUTES,
                _CHRONO_MONDAY,
                _CHRONO_TUESDAY,
                _CHRONO_WEDNESDAY,
                _CHRONO_THURSDAY,
                _CHRONO_FRIDAY,
                _CHRONO_SATURDAY,
                _CHRONO_SUNDAY,
                _GEO_LATITUDE,
                _GEO_LONG,
                _GEO_RADIUS_METRES
        };
        public static final String CREATE_TABLE_SQL = "CREATE TABLE " +
                TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + _TITLE + " TEXT, "
                + _CHRONO_START_HOUR + " INTEGER, "
                + _CHRONO_START_MINUTES + " INTEGER, "
                + _CHRONO_STOP_HOUR + " INTEGER, "
                + _CHRONO_STOP_MINUTES + " INTEGER, "
                + _CHRONO_MONDAY + " INTEGER, "
                + _CHRONO_TUESDAY + " INTEGER, "
                + _CHRONO_WEDNESDAY + " INTEGER, "
                + _CHRONO_THURSDAY + " INTEGER, "
                + _CHRONO_FRIDAY + " INTEGER, "
                + _CHRONO_SATURDAY + " INTEGER, "
                + _CHRONO_SUNDAY + " INTEGER, "
                + _GEO_LATITUDE + " DOUBLE, "
                + _GEO_LONG + " DOUBLE, "
                + _GEO_RADIUS_METRES + " INTEGER) ";
    }


}
