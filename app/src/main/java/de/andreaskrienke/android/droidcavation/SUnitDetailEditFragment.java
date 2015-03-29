package de.andreaskrienke.android.droidcavation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;
import de.andreaskrienke.android.droidcavation.util.DateChooser;
import de.andreaskrienke.android.droidcavation.util.SpinnerObject;
import de.andreaskrienke.android.droidcavation.util.Utility;

/**
 * SUnitDetailEditFragment class.
 */
public class SUnitDetailEditFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitDetailEditFragment.class.getSimpleName();

    private static final int SUNIT_DETAIL_EDIT_LOADER = 1;

    // These indices are tied to SUNIT_COLUMNS.  If SUNIT_COLUMNS changes, these
    // must change.
    static final int COLUMN_SUNIT_ID = 0;
    static final int COLUMN_SUNIT_NUMBER = 1;
    static final int COLUMN_AREA_ID = 2;
    static final int COLUMN_SQUARE_ID = 3;
    static final int COLUMN_TOP_SURFACE_DATE = 4;
    static final int COLUMN_BOTTOM_SURFACE_DATE = 5;
    static final int COLUMN_TACHY_MEASUREMENTS = 6;
    static final int COLUMN_TM_TOP_SURFACE_OUTLINE = 7;
    static final int COLUMN_TM_BOTTOM_SURFACE_OUTLINE = 8;
    static final int COLUMN_TM_LEVELMENTS = 9;
    static final int COLUMN_SHAPE = 10;
    static final int COLUMN_COLOR = 11;
    static final int COLUMN_SEDIMENT_TYPE = 12;
    static final int COLUMN_SEDIMENT_SIZE = 13;
    static final int COLUMN_SEDIMENT_PERCENTAGE = 14;
    static final int COLUMN_SHORT_DESC = 15;
    static final int COLUMN_SUNIT_TOP_ID = 16;
    static final int COLUMN_SUNIT_BOTTOM_ID = 17;
    static final int COLUMN_ASSOCIATED_FEATURE = 18;
    static final int COLUMN_FINDS_CHARCOAL = 19;
    static final int COLUMN_FINDS_POTTERY = 20;
    static final int COLUMN_FINDS_BONE = 21;
    static final int COLUMN_FINDS_FAIENCE = 22;
    static final int COLUMN_FINDS_SHELL = 23;
    static final int COLUMN_FINDS_WOOD = 24;
    static final int COLUMN_FINDS_CLAY_MUD = 26;
    static final int COLUMN_FINDS_OTHERS = 27;
    static final int COLUMN_DATING_DESCRIPTION = 28;
    static final int COLUMN_SKETCH = 29;
    static final int COLUMN_EXCAVATION_DATE_BEGIN = 30;
    static final int COLUMN_EXCAVATION_DATE_END = 31;
    static final int COLUMN_EXCAVATED_BY = 31;

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

        /**
         * Structure from motion model
         */
        // top surface date
        Button btnTopSurfaceDate = (Button) rootView.findViewById(R.id.sunit_top_surface_date);
        DateChooser topSurfaceDate = new DateChooser(btnTopSurfaceDate, getActivity());

        // bottom surface date
        Button btnBottomSurfaceDate = (Button) rootView.findViewById(R.id.sunit_bottom_surface_date);
        DateChooser bottomSurfaceDate = new DateChooser(btnBottomSurfaceDate, getActivity());

        // set SUNIT shape
        Spinner sUnitShape = (Spinner) rootView.findViewById(R.id.spin_sunit_shape);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.shapes));
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sUnitShape.setAdapter(shapeAdapter);

        // set SUNIT top spinner entries from DB
        Spinner sUnitTop = (Spinner) rootView.findViewById(R.id.spin_sunit_top);

        // Spinner Drop down elements
        List<SpinnerObject> sUnitList = getSUList();

        // Creating adapter for spinner
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(getActivity(),
                android.R.layout.simple_spinner_item, sUnitList);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
        //        R.array.shapes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sUnitTop.setAdapter(dataAdapter);


        Spinner sUnitBottom = (Spinner) rootView.findViewById(R.id.spin_sunit_bottom);

        //int hidingItemIndex = 0;

        //CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(getActivity(),
        //        android.R.layout.simple_spinner_item, sUnitList, hidingItemIndex);
        //customArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sUnitBottom.setAdapter(dataAdapter);

        initListeners(rootView);

        return rootView;
    }

    private void initListeners(View view) {
        //our activity is click listener for btn_sunit_save
        Button btnSave = (Button)view.findViewById(R.id.btn_sunit_save);
        btnSave.setOnClickListener(this);
        Button btnCancel = (Button)view.findViewById(R.id.btn_sunit_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String message = "";

        switch (v.getId()) {
            // Cancel
            case R.id.btn_sunit_cancel:
                getActivity().finish();
                break;
            // Save
            case R.id.btn_sunit_save:

                long sUnitId;
                int updatedRecords;

                // get values from view
                ContentValues sUnitValues = getContentValuesFromView();

                //EditText numberEditText = (EditText) getActivity().findViewById(R.id.edit_sunit_number);
                //TextView idEditText = (TextView) getActivity().findViewById(R.id.sunit_id);

                Uri sUnitUri = getActivity().getIntent().getData();

                if (sUnitUri != null) {

                    // we have an Uri from intent data -> get ID
                    sUnitId = DroidCavationContract.SUnitEntry.getSUnitIdFromUri(sUnitUri);

                    // try to read DB with given ID
                    Cursor sUnitCursor = getActivity().getContentResolver().query(
                                DroidCavationContract.SUnitEntry.CONTENT_URI,
                                new String[]{DroidCavationContract.SUnitEntry._ID},
                                DroidCavationContract.SUnitEntry._ID + " = ?",
                                new String[]{Long.toString(sUnitId)},
                                null);

                    if (sUnitCursor.moveToFirst()) {
                        // entry exists -> update
                        updatedRecords = getActivity().getContentResolver().update(
                                DroidCavationContract.SUnitEntry.buildSUnitUri(
                                        sUnitCursor.getLong(sUnitCursor.getColumnIndex(
                                                DroidCavationContract.SUnitEntry._ID))),
                                sUnitValues,
                                null,
                                null);

                        message = getResources().getString(R.string.sunit_update_message)
                                  + " - " + Long.toString(sUnitId);

                    }
                    sUnitCursor.close();

                    getActivity().finish();
                }
                else {
                    // no intent data -> create new DB entry

                    try {
                        Uri insertedUri = getActivity().getContentResolver().insert(
                                DroidCavationContract.SUnitEntry.CONTENT_URI, sUnitValues);

                        //The resulting URI contains the ID for the row.
                        // Extract the sUnitId from the Uri.
                        sUnitId = ContentUris.parseId(insertedUri);

                        message = getResources().getString(R.string.sunit_insert_message)
                                    + " - " + Long.toString(sUnitId);

                        getActivity().finish();
                    }
                    catch (SQLException ex) {
                        message = ex.getMessage();
                    }

                }


                Toast.makeText(getActivity().getBaseContext(),
                        message, Toast.LENGTH_LONG).show();

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
                DroidCavationContract.SUnitEntry.SUNIT_COLUMNS,
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

        // sunit id
        int id = data.getInt(COLUMN_SUNIT_ID);

        TextView txtId = (TextView)getView().findViewById(R.id.sunit_id);
        txtId.setText(String.valueOf(id));

        // sunit number
        int number = data.getInt(COLUMN_SUNIT_NUMBER);

        EditText txtNumber = (EditText)getView().findViewById(R.id.sunit_number);
        txtNumber.setText(String.valueOf(number));

        // sunit top surface date
        long topSurfaceDate = data.getLong(COLUMN_TOP_SURFACE_DATE);
        if (topSurfaceDate > 0) {
            Button btnTopSurfaceDate = (Button) getView().findViewById(R.id.sunit_top_surface_date);
            btnTopSurfaceDate.setText(Utility.sdformat.format(Utility.loadDate(topSurfaceDate)));
        }
        // sunit bottom surface date
        long bottomSurfaceDate = data.getLong(COLUMN_BOTTOM_SURFACE_DATE);
        if (bottomSurfaceDate > 0) {
            Button btnBottomSurfaceDate = (Button) getView().findViewById(R.id.sunit_bottom_surface_date);
            btnBottomSurfaceDate.setText(Utility.sdformat.format(Utility.loadDate(bottomSurfaceDate)));
        }

        // sunit shape
        String shape = data.getString(COLUMN_SHAPE);

        Spinner spShape = (Spinner)getView().findViewById(R.id.spin_sunit_shape);
        int shapePos = ((ArrayAdapter<String>) spShape.getAdapter()).getPosition(shape);
        spShape.setSelection(shapePos);

        // sunit sediment type
        String sedimentType = data.getString(COLUMN_SEDIMENT_TYPE);

        Spinner spSedimentType = (Spinner)getView().findViewById(R.id.spin_sunit_sediment_type);
        int sedimentTypePos = ((ArrayAdapter<String>) spSedimentType.getAdapter()).getPosition(sedimentType);
        spSedimentType.setSelection(sedimentTypePos);

        // sunit sediment size
        String sedimentSize = data.getString(COLUMN_SEDIMENT_SIZE);

        Spinner spSedimentSize = (Spinner)getView().findViewById(R.id.spin_sunit_sediment_size);
        int sedimentSizePos = ((ArrayAdapter<String>) spSedimentSize.getAdapter()).getPosition(sedimentSize);
        spSedimentSize.setSelection(sedimentSizePos);

        // sunit sediment percentage
        int sedimentPercentage = data.getInt(COLUMN_SEDIMENT_PERCENTAGE);

        SeekBar sbSedimentPercentage = (SeekBar)getView().findViewById(R.id.sunit_sediment_percentage);
        sbSedimentPercentage.setProgress(sedimentPercentage);

        // sunit short description
        String shortDesc = data.getString(COLUMN_SHORT_DESC);

        EditText txtShortDesc = (EditText)getView().findViewById(R.id.sunit_short_desc);
        txtShortDesc.setText(shortDesc);

        // sunit top id
        int sUnitTopId = data.getInt(COLUMN_SUNIT_TOP_ID);

        Spinner spSUnitTop = (Spinner)getView().findViewById(R.id.spin_sunit_top);
        ArrayAdapter<SpinnerObject> sUnitTopAdapter = (ArrayAdapter<SpinnerObject>) spSUnitTop.getAdapter();
        int sUnitTopPos = sUnitTopAdapter.getPosition(new SpinnerObject(sUnitTopId, String.valueOf(number)));
        spSUnitTop.setSelection(sUnitTopPos);

        // sunit bottom id
        int sUnitBottomId = data.getInt(COLUMN_SUNIT_BOTTOM_ID);

        Spinner spSUnitBottom = (Spinner)getView().findViewById(R.id.spin_sunit_bottom);
        ArrayAdapter<SpinnerObject> sUnitBottomAdapter = (ArrayAdapter<SpinnerObject>) spSUnitBottom.getAdapter();
        int sUnitBottomPos = sUnitBottomAdapter.getPosition(new SpinnerObject(sUnitBottomId, String.valueOf(number)));
        spSUnitBottom.setSelection(sUnitBottomPos);

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

        EditText txtNumber = (EditText)getView().findViewById(R.id.sunit_number);
        String sUnitNumber = txtNumber.getText().toString();

        Button btnTopSurfaceDate = (Button)getView().findViewById(R.id.sunit_top_surface_date);
        long topSurfaceDate = 0;
        try {
            topSurfaceDate = Utility.persistDate(
                                    Utility.sdformat.parse(btnTopSurfaceDate.getText().toString()));
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }

        Button btnBottomSurfaceDate = (Button)getView().findViewById(R.id.sunit_bottom_surface_date);
        long bottomSurfaceDate = 0;
        try {
            bottomSurfaceDate = Utility.persistDate(
                                    Utility.sdformat.parse(btnBottomSurfaceDate.getText().toString()));
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }

        Spinner spShape = (Spinner)getView().findViewById(R.id.spin_sunit_shape);
        String shape = spShape.getSelectedItem().toString();

        Spinner spSedimentType = (Spinner)getView().findViewById(R.id.spin_sunit_sediment_type);
        String sedimentType = spSedimentType.getSelectedItem().toString();

        Spinner spSedimentSize = (Spinner)getView().findViewById(R.id.spin_sunit_sediment_size);
        String sedimentSize = spSedimentSize.getSelectedItem().toString();

        // sunit sediment percentage
        SeekBar sbSedimentPercentage = (SeekBar)getView().findViewById(R.id.sunit_sediment_percentage);
        int sedimentPercentage = sbSedimentPercentage.getProgress();

        EditText detailSUnitShortDescTextView = (EditText)getView().findViewById(R.id.sunit_short_desc);
        String sUnitShortDesc = detailSUnitShortDescTextView.getText().toString();

        Spinner spSUnitTopId = (Spinner)getView().findViewById(R.id.spin_sunit_top);
        int sUnitTopId = ((SpinnerObject) spSUnitTopId.getSelectedItem()).getId();

        Spinner spSUnitBottomId = (Spinner)getView().findViewById(R.id.spin_sunit_bottom);
        int sUnitBottomId = ((SpinnerObject) spSUnitBottomId.getSelectedItem()).getId();


        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_NUMBER, sUnitNumber);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_TOP_SURFACE_DATE, topSurfaceDate);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_BOTTOM_SURFACE_DATE, bottomSurfaceDate);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SHAPE, shape);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SEDIMENT_TYPE, sedimentType);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SEDIMENT_SIZE, sedimentSize);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SEDIMENT_PERCENTAGE, sedimentPercentage);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SHORT_DESC, sUnitShortDesc);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SUNIT_TOP_ID, sUnitTopId);
        contentValues.put(DroidCavationContract.SUnitEntry.COLUMN_SUNIT_BOTTOM_ID, sUnitBottomId);

        return contentValues;
    }

    private List<SpinnerObject> getSUList() {

        List<SpinnerObject> sUnitList = new ArrayList < SpinnerObject > ();

        Cursor sUnitCursor = getActivity().getContentResolver().query(
                DroidCavationContract.SUnitEntry.CONTENT_URI,
                DroidCavationContract.SUnitEntry.SUNIT_COLUMNS,
                null,
                null,
                null);

        Uri sUnitUri = getActivity().getIntent().getData();

        int sUnitId = 0;
        if (sUnitUri != null) {

            // we have an Uri from intent data -> get ID
            sUnitId = DroidCavationContract.SUnitEntry.getSUnitIdFromUri(sUnitUri);
        }

        // add Default entry to list
        sUnitList.add(new SpinnerObject(0, ""));

        // looping through all rows and adding to list
        if (sUnitCursor.moveToFirst()) {
            do {
                if (sUnitId == sUnitCursor.getInt(sUnitCursor.getColumnIndex(
                                                  DroidCavationContract.SUnitEntry._ID))) {
                    // don't add current SUNIT to list of related SUNITs
                    continue;
                }
                // adding SUNIT to list via Spinner Object containing ID and NUMBER
                sUnitList.add(new SpinnerObject(
                                    sUnitCursor.getInt(sUnitCursor.getColumnIndex(
                                            DroidCavationContract.SUnitEntry._ID)),
                                    sUnitCursor.getString(sUnitCursor.getColumnIndex(
                                            DroidCavationContract.SUnitEntry.COLUMN_NUMBER))
                ));



            } while (sUnitCursor.moveToNext());
        }
        // closing connection
        sUnitCursor.close();

        return sUnitList;
    }
}