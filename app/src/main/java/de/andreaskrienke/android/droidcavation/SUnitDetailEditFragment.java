package de.andreaskrienke.android.droidcavation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;

/**
 * SUnitDetailEditFragment class.
 */
public class SUnitDetailEditFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitDetailEditFragment.class.getSimpleName();

    private static final int SUNIT_DETAIL_LOADER = 0;

    // For the sunit view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] SUNIT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name,
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            DroidCavationContract.SUnitEntry.TABLE_NAME + "." + DroidCavationContract.SUnitEntry._ID,
            DroidCavationContract.SUnitEntry.COLUMN_NUMBER,
            DroidCavationContract.SUnitEntry.COLUMN_SHORT_DESC
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_SUNIT_ID = 0;
    static final int COL_SUNIT_NUMBER = 1;
    static final int COL_SUNIT_SHORT_DESC = 2;

    public SUnitDetailEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_sunit_detail, container, false);

        View rootView = inflater.inflate(R.layout.fragment_sunit_detail_edit, container, false);

        // The detail Activity called via intent.
//        Intent intent = getActivity().getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            String sUnitStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//            ((TextView) rootView.findViewById(R.id.edit_sunit_number))
//                    .setText(sUnitStr);
//        }

        initListeners(rootView);

        return rootView;
    }

    private void initListeners(View view) {
        //our activity is click listener for btn_sunit_save
        Button btn = (Button)view.findViewById(R.id.btn_sunit_save);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sunit_save) {
            EditText numberEditText = (EditText) getActivity().findViewById(R.id.edit_sunit_number);
            if (numberEditText != null) {
                String numberText = numberEditText.getText().toString();
                int number = Integer.parseInt(numberText);
                long id = addSUnit(number, "short desc");

                Toast.makeText(getActivity().getBaseContext(),
                        Long.toString(id), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUNIT_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                SUNIT_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if (!data.moveToFirst()) {
            return;
        }

        // sunit number
        int number = data.getInt(COL_SUNIT_NUMBER);

        TextView detailSUnitNumberTextView = (TextView)getView().findViewById(R.id.sunit_number);
        detailSUnitNumberTextView.setText(String.valueOf(number));

        // sunit short description
        String sUnitShortDesc = data.getString(COL_SUNIT_SHORT_DESC);

        TextView detailSUnitShortDescTextView = (TextView)getView().findViewById(R.id.sunit_short_desc);
        detailSUnitShortDescTextView.setText(sUnitShortDesc);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Helper method to handle insertion of a new sunit in the database.
     *
     * @param number The unique number for the sunit.
     * @param short_desc A human-readable description.
     * @return the row ID of the added sunit.
     */
    long addSUnit(int number, String short_desc) {

        long sUnitId;

        // First, check if the location with this city name exists in the db
        Cursor sUnitCursor = getActivity().getContentResolver().query(
                DroidCavationContract.SUnitEntry.CONTENT_URI,
                new String[]{DroidCavationContract.SUnitEntry._ID},
                DroidCavationContract.SUnitEntry.COLUMN_NUMBER + " = ?",
                new String[]{Integer.toString(number)},
                null);

        if (sUnitCursor.moveToFirst()) {

            int numberIdIndex = sUnitCursor.getColumnIndex(DroidCavationContract.SUnitEntry._ID);
            sUnitId = sUnitCursor.getLong(numberIdIndex);

        }
        else {

            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues sUnitValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            sUnitValues.put(DroidCavationContract.SUnitEntry.COLUMN_NUMBER, number);
            sUnitValues.put(DroidCavationContract.SUnitEntry.COLUMN_SHORT_DESC, short_desc);

            // Finally, insert sunit data into the database.
            Uri insertedUri = getActivity().getContentResolver().insert(
                    DroidCavationContract.SUnitEntry.CONTENT_URI,
                    sUnitValues
            );

            // The resulting URI contains the ID for the row.  Extract the sUnitId from the Uri.
            sUnitId = ContentUris.parseId(insertedUri);
        }

        sUnitCursor.close();
        // Wait, that worked?  Yes!
        return sUnitId;
    }
}