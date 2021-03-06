package de.andreaskrienke.android.droidcavation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract.SUnitEntry;


/**
 * DroidCavation Database Helper Class for Sqlite.
 */
public class DroidCavationDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "droidcavation.db";

    public DroidCavationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SUNIT_TABLE = "CREATE TABLE " + SUnitEntry.TABLE_NAME + " (" +
                SUnitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SUnitEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                SUnitEntry.COLUMN_AREA_ID + " INTEGER, " +
                SUnitEntry.COLUMN_SQUARE_ID + " INTEGER, " +
                SUnitEntry.COLUMN_TOP_SURFACE_DATE + " DATE, " +
                SUnitEntry.COLUMN_BOTTOM_SURFACE_DATE + " DATE, " +
                SUnitEntry.COLUMN_TACHY_MEASUREMENTS + " INTEGER, " +
                SUnitEntry.COLUMN_TM_TOP_SURFACE_OUTLINE + " INTEGER, " +
                SUnitEntry.COLUMN_TM_BOTTOM_SURFACE_OUTLINE + " INTEGER, " +
                SUnitEntry.COLUMN_TM_LEVELMENTS + " INTEGER, " +
                SUnitEntry.COLUMN_SHAPE + " TEXT, " +
                SUnitEntry.COLUMN_COLOR + " TEXT, " +
                SUnitEntry.COLUMN_SEDIMENT_TYPE + " TEXT, " +
                SUnitEntry.COLUMN_SEDIMENT_SIZE + " TEXT, " +
                SUnitEntry.COLUMN_SEDIMENT_PERCENTAGE + " INTEGER, " +
                SUnitEntry.COLUMN_SHORT_DESC + " TEXT, " +
                SUnitEntry.COLUMN_SUNIT_TOP_ID + " INTEGER, " +
                SUnitEntry.COLUMN_SUNIT_BOTTOM_ID + " INTEGER, " +
                SUnitEntry.COLUMN_ASSOCIATED_FEATURE + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_CHARCOAL + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_POTTERY + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_BONE + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_FAIENCE + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_SHELL + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_WOOD + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_CLAY_MUD + " INTEGER, " +
                SUnitEntry.COLUMN_FINDS_OTHERS + " TEXT, " +
                SUnitEntry.COLUMN_DATING_DESCRIPTION + " TEXT, " +
                SUnitEntry.COLUMN_SKETCH + " BLOB, " +
                SUnitEntry.COLUMN_EXCAVATION_DATE_BEGIN + " DATE, " +
                SUnitEntry.COLUMN_EXCAVATION_DATE_END + " DATE, " +
                SUnitEntry.COLUMN_EXCAVATED_BY + " TEXT, " +

                // To assure the application have just one sunit entry
                // it's created a UNIQUE constraint with REPLACE strategy
                //" UNIQUE (" + SUnitEntry.COLUMN_NUMBER + ") ON CONFLICT REPLACE);";
                " UNIQUE (" + SUnitEntry.COLUMN_NUMBER + ") );";

        sqLiteDatabase.execSQL(SQL_CREATE_SUNIT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SUnitEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}