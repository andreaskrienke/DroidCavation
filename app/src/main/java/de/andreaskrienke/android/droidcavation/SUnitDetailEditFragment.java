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
import android.widget.Toast;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;

/**
 * SUnitDetailEditFragment class.
 */
public class SUnitDetailEditFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitDetailEditFragment.class.getSimpleName();

    private static final int SUNIT_DETAIL_EDIT_LOADER = 1;

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

        switch (v.getId()) {
            // Save
            case R.id.btn_sunit_save:

                // get values from view
                ContentValues sUnitValues = getContentValuesFromView();

                EditText numberEditText = (EditText) getActivity().findViewById(R.id.edit_sunit_number);

                if (numberEditText != null) {

                    long sUnitId;

                    // First, check if the entry the db
                    String numberText = numberEditText.getText().toString();

                    Cursor sUnitCursor = getActivity().getContentResolver().query(
                            DroidCavationContract.SUnitEntry.CONTENT_URI,
                            new String[]{DroidCavationContract.SUnitEntry._ID},
                            DroidCavationContract.SUnitEntry.COLUMN_NUMBER + " = ?",
                            new String[]{numberText},
                            null);

                    if (sUnitCursor.moveToFirst()) {
                        // entry exists -> update
                        sUnitId = getActivity().getContentResolver().update(
                                DroidCavationContract.SUnitEntry.buildSUnitUri(
                                        sUnitCursor.getLong(sUnitCursor.getColumnIndex(
                                                DroidCavationContract.SUnitEntry._ID))),
                                sUnitValues, null, null);


                    }
                    else {
                        // entry does not exist -> insert
                        Uri insertedUri = getActivity().getContentResolver().insert(
                                DroidCavationContract.SUnitEntry.CONTENT_URI, sUnitValues);

                        //The resulting URI contains the ID for the row.
                        // Extract the sUnitId from the Uri.
                        sUnitId = ContentUris.parseId(insertedUri);

                    }

                    sUnitCursor.close();

                    getActivity().finish();

                    Toast.makeText(getActivity().getBaseContext(),
                            Long.toString(sUnitId), Toast.LENGTH_LONG).show();
                }

                break;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUNIT_DETAIL_EDIT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        Uri sUnitforIdUri = intent.getData();
        if (sUnitforIdUri == null) {
            //sUnitforIdUri = DroidCavationContract.SUnitEntry.CONTENT_URI;
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                sUnitforIdUri,
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

        EditText detailSUnitNumberTextView = (EditText)getView().findViewById(R.id.edit_sunit_number);
        detailSUnitNumberTextView.setText(String.valueOf(number));

        // sunit short description
        String sUnitShortDesc = data.getString(COL_SUNIT_SHORT_DESC);

        EditText detailSUnitShortDescTextView = (EditText)getView().findViewById(R.id.edit_sunit_short_desc);
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
     * Helper method to construct ContentValues from current view fields.
     *
     * @return ContentValues
     */
    private ContentValues getContentValuesFromView() {
        ContentValues contentValues = new ContentValues();

        // read all fields from view
        EditText detailSUnitNumberTextView = (EditText)getView().findViewById(R.id.edit_sunit_number);
        String sUnitNumber = detailSUnitNumberTextView.getText().toString();

        EditText detailSUnitShortDescTextView = (EditText)getView().findViewById(R.id.edit_sunit_short_desc);
        String sUnitShortDesc = detailSUnitShortDescTextView.getText().toString();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_NUMBER, sUnitNumber);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SHORT_DESC, sUnitShortDesc);

        return contentValues;
    }
}