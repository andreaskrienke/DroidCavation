package de.andreaskrienke.android.droidcavation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class SUnitDetailActivityFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = SUnitDetailActivityFragment.class.getSimpleName();

    public SUnitDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_sunit_detail, container, false);

        View rootView = inflater.inflate(R.layout.fragment_sunit_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String sUnitStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.edit_sunit_number))
                    .setText(sUnitStr);
        }

        initListeners();

        return rootView;
    }

    private void initListeners() {
        //our activity is click listener for btn_sunit_save
        getActivity().findViewById(R.id.btn_sunit_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sunit_save) {
            long id = addSUnit(
                    Integer.getInteger(((EditText) getActivity().findViewById(R.id.edit_sunit_number)).getText().toString()),
                    "short desc");

            Toast.makeText(getActivity().getBaseContext(),
                    Long.toString(id), Toast.LENGTH_LONG).show();
        }
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
