package de.andreaskrienke.android.droidcavation.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * DroidCavation ContentProvider class
 */
public class DroidCavationProvider extends ContentProvider {

    private static final String LOG_TAG = DroidCavationProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DroidCavationDbHelper mOpenHelper;

    static final int SUNIT_LIST = 100;
    static final int SUNIT_ID   = 101;

    private static final SQLiteQueryBuilder sSUnitQueryBuilder;

    static{
        sSUnitQueryBuilder = new SQLiteQueryBuilder();

        // Which tables to work on
        sSUnitQueryBuilder.setTables(
                DroidCavationContract.SUnitEntry.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = DroidCavationContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DroidCavationContract.PATH_SUNIT, SUNIT_LIST);
        matcher.addURI(authority, DroidCavationContract.PATH_SUNIT + "/#", SUNIT_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DroidCavationDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SUNIT_ID:
                return DroidCavationContract.SUnitEntry.CONTENT_ITEM_TYPE;
            case SUNIT_LIST:
                return DroidCavationContract.SUnitEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            // "SUNIT/#"
            case SUNIT_ID:
            {
                // for single row -> get _ID from URI
                retCursor = getSUnitById(uri, projection, sortOrder);
                break;
            }
            // "SUNIT"
            case SUNIT_LIST: {

                retCursor = mOpenHelper.getReadableDatabase().query(
                        DroidCavationContract.SUnitEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            // "SUNIT"
            case SUNIT_LIST: {

                long _id = db.insert(DroidCavationContract.SUnitEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DroidCavationContract.SUnitEntry.buildSUnitUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            // "SUNIT/#"
            case SUNIT_ID: {
                // update single row -> get _ID from URI
                int id = DroidCavationContract.SUnitEntry.getSUnitIdFromUri(uri);

                // extend selection string with _ID
                selection = DroidCavationContract.SUnitEntry._ID + " = " + id +
                            (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");

                rowsUpdated = db.update(DroidCavationContract.SUnitEntry.TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);
                break;
            }
            case SUNIT_LIST: {

                rowsUpdated = db.update(DroidCavationContract.SUnitEntry.TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    private Cursor getSUnitById(Uri uri, String[] projection, String sortOrder) {

        int id = DroidCavationContract.SUnitEntry.getSUnitIdFromUri(uri);

        return sSUnitQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sSUnitIdSelection,
                new String[]{Integer.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    //sunit._ID = ?
    private static final String sSUnitIdSelection =
            DroidCavationContract.SUnitEntry.TABLE_NAME+
                    "." + DroidCavationContract.SUnitEntry._ID + " = ? ";

    //sunit.number = ?
    private static final String sSUnitNumberSelection =
            DroidCavationContract.SUnitEntry.TABLE_NAME+
                    "." + DroidCavationContract.SUnitEntry.COLUMN_NUMBER + " = ? ";

}
