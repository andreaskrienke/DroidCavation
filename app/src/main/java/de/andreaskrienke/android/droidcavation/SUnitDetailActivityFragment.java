package de.andreaskrienke.android.droidcavation;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;
import de.andreaskrienke.android.droidcavation.util.SpinnerObject;
import de.andreaskrienke.android.droidcavation.util.Utility;


/**
 * a placeholder fragment containing a simple view.
 */
public class SUnitDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitDetailActivityFragment.class.getSimpleName();

    private static final int SUNIT_DETAIL_LOADER = 0;

    static final String DETAIL_URI = "URI";

    private Uri mUri;

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
    static final int COLUMN_FINDS_CLAY_MUD = 25;
    static final int COLUMN_FINDS_OTHERS = 26;
    static final int COLUMN_DATING_DESCRIPTION = 27;
    static final int COLUMN_SKETCH = 28;
    static final int COLUMN_EXCAVATION_DATE_BEGIN = 29;
    static final int COLUMN_EXCAVATION_DATE_END = 30;
    static final int COLUMN_EXCAVATED_BY = 31;

    public SUnitDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(SUnitDetailActivityFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_sunit_detail, container, false);

        // disable seekbar
        SeekBar sedimentPercentage = (SeekBar) rootView.findViewById(R.id.sunit_sediment_percentage);
        sedimentPercentage.setEnabled(false);

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUNIT_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.v(LOG_TAG, "In onCreateLoader");
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DroidCavationContract.SUnitEntry.SUNIT_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;

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
        TextView txtNumber = (TextView)getView().findViewById(R.id.sunit_number);
        txtNumber.setText(String.valueOf(number));

        // sunit area id
        String area = data.getString(COLUMN_AREA_ID);
        TextView spArea = (TextView)getView().findViewById(R.id.spin_sunit_area_id);
        //int areaPos = ((ArrayAdapter<String>) spArea.getAdapter()).getPosition(area);
        //spArea.setSelection(areaPos);
        spArea.setText(area);

        // sunit square id
        String square = data.getString(COLUMN_SQUARE_ID);
        TextView spSquare = (TextView)getView().findViewById(R.id.spin_square_id);
        //int squarePos = ((ArrayAdapter<String>) spSquare.getAdapter()).getPosition(square);
        //spSquare.setSelection(squarePos);
        spSquare.setText(square);

        // sunit top surface date
        long topSurfaceDate = data.getLong(COLUMN_TOP_SURFACE_DATE);
        if (topSurfaceDate > 0) {
            TextView btnTopSurfaceDate = (TextView) getView().findViewById(R.id.sunit_top_surface_date);
            btnTopSurfaceDate.setText(Utility.sdformat.format(Utility.loadDate(topSurfaceDate)));
        }
        // sunit bottom surface date
        long bottomSurfaceDate = data.getLong(COLUMN_BOTTOM_SURFACE_DATE);
        if (bottomSurfaceDate > 0) {
            TextView btnBottomSurfaceDate = (TextView) getView().findViewById(R.id.sunit_bottom_surface_date);
            btnBottomSurfaceDate.setText(Utility.sdformat.format(Utility.loadDate(bottomSurfaceDate)));
        }

        // tachy measurements
        int tachyMeasurements = data.getInt(COLUMN_TACHY_MEASUREMENTS);
        ToggleButton tgTachyMeasurements = (ToggleButton) getView().findViewById(R.id.sunit_tachy_measurements);
        tgTachyMeasurements.setChecked(tachyMeasurements > 0 ? true : false);

        // top surface outline
        int topSurfaceOutline = data.getInt(COLUMN_TM_TOP_SURFACE_OUTLINE);
        CheckBox cbTopSurfaceOutline = (CheckBox) getView().findViewById(R.id.sunit_top_surface_outline);
        cbTopSurfaceOutline.setChecked(topSurfaceOutline > 0 ? true : false);

        // bottom surface outline
        int bottomSurfaceOutline = data.getInt(COLUMN_TM_BOTTOM_SURFACE_OUTLINE);
        CheckBox tgBottomSurfaceOutline = (CheckBox) getView().findViewById(R.id.sunit_bottom_surface_outline);
        tgBottomSurfaceOutline.setChecked(bottomSurfaceOutline > 0 ? true : false);

        // levelments
        int levelments = data.getInt(COLUMN_TM_LEVELMENTS);
        CheckBox cbLevelements = (CheckBox) getView().findViewById(R.id.sunit_levelments);
        cbLevelements.setChecked(levelments > 0 ? true : false);

        // sunit shape
        String shape = data.getString(COLUMN_SHAPE);
        TextView spShape = (TextView)getView().findViewById(R.id.spin_sunit_shape);
        //int shapePos = ((ArrayAdapter<String>) spShape.getAdapter()).getPosition(shape);
        //spShape.setSelection(shapePos);
        spShape.setText(shape);

        // sunit sediment type
        String sedimentType = data.getString(COLUMN_SEDIMENT_TYPE);
        TextView spSedimentType = (TextView)getView().findViewById(R.id.spin_sunit_sediment_type);
        //int sedimentTypePos = ((ArrayAdapter<String>) spSedimentType.getAdapter()).getPosition(sedimentType);
        //spSedimentType.setSelection(sedimentTypePos);
        spSedimentType.setText(sedimentType);

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
        TextView txtShortDesc = (TextView)getView().findViewById(R.id.sunit_short_desc);
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

        // finds charcoal
        int findsCharcoal = data.getInt(COLUMN_FINDS_CHARCOAL);
        CheckBox cbFindsCharcoal = (CheckBox) getView().findViewById(R.id.sunit_finds_charcoal);
        cbFindsCharcoal.setChecked(findsCharcoal > 0 ? true : false);

        // finds pottery
        int findsPottery = data.getInt(COLUMN_FINDS_POTTERY);
        CheckBox cbFindsPottery = (CheckBox) getView().findViewById(R.id.sunit_finds_pottery);
        cbFindsPottery.setChecked(findsPottery > 0 ? true : false);

        // finds bone
        int findsBone = data.getInt(COLUMN_FINDS_BONE);
        CheckBox cbFindsBone = (CheckBox) getView().findViewById(R.id.sunit_finds_bone);
        cbFindsBone.setChecked(findsBone > 0 ? true : false);

        // finds faience
        int findsFaience = data.getInt(COLUMN_FINDS_FAIENCE);
        CheckBox cbFindsFaience = (CheckBox) getView().findViewById(R.id.sunit_finds_faience);
        cbFindsFaience.setChecked(findsFaience > 0 ? true : false);

        // finds shell
        int findsShell = data.getInt(COLUMN_FINDS_SHELL);
        CheckBox cbFindsShell = (CheckBox) getView().findViewById(R.id.sunit_finds_shell);
        cbFindsShell.setChecked(findsShell > 0 ? true : false);

        // finds wood
        int findsWood = data.getInt(COLUMN_FINDS_WOOD);
        CheckBox cbFindsWood = (CheckBox) getView().findViewById(R.id.sunit_finds_wood);
        cbFindsWood.setChecked(findsWood > 0 ? true : false);

        // finds clay/mud
        int findsClayMud = data.getInt(COLUMN_FINDS_CLAY_MUD);
        CheckBox cbFindsClayMud = (CheckBox) getView().findViewById(R.id.sunit_finds_clay_mud);
        cbFindsClayMud.setChecked(findsClayMud > 0 ? true : false);

        // finds other
        String findsOther = data.getString(COLUMN_FINDS_OTHERS);
        TextView txtFindsOther = (TextView)getView().findViewById(R.id.sunit_finds_other);
        txtFindsOther.setText(findsOther);

        // dating description
        String datingDesc = data.getString(COLUMN_DATING_DESCRIPTION);
        TextView txtDatingDesc = (TextView)getView().findViewById(R.id.sunit_dating_desc);
        txtDatingDesc.setText(datingDesc);

        // excavation date begin
        long excavationDateBegin = data.getLong(COLUMN_EXCAVATION_DATE_BEGIN);
        if (excavationDateBegin > 0) {
            Button btnExcavationDateBegin = (Button) getView().findViewById(R.id.sunit_excavation_begin);
            btnExcavationDateBegin.setText(Utility.sdformat.format(Utility.loadDate(excavationDateBegin)));
        }
        // excavation date end
        long excavationDateEnd = data.getLong(COLUMN_EXCAVATION_DATE_END);
        if (excavationDateEnd > 0) {
            Button btnExcavationDateEnd = (Button) getView().findViewById(R.id.sunit_excavation_end);
            btnExcavationDateEnd.setText(Utility.sdformat.format(Utility.loadDate(excavationDateEnd)));
        }

        // excavated by
        String excavatedBy = data.getString(COLUMN_EXCAVATED_BY);
        TextView txtExcavatedBy = (TextView)getView().findViewById(R.id.sunit_excavated_by);
        txtExcavatedBy.setText(excavatedBy);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<SpinnerObject> getSUList() {

        List<SpinnerObject> sUnitList = new ArrayList< SpinnerObject >();

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
