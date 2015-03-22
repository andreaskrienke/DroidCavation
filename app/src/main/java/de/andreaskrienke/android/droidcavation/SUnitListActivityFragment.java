package de.andreaskrienke.android.droidcavation;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class SUnitListActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitListActivityFragment.class.getSimpleName();

    private static final int SUNIT_LOADER = 0;
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

    private SUnitListAdapter mSUnitListAdapter;

    public SUnitListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mSUnitListAdapter = new SUnitListAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_sunit_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_sunit);
        listView.setAdapter(mSUnitListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    Intent intent = new Intent(getActivity(), SUnitDetailActivity.class)
                            .setData(DroidCavationContract.SUnitEntry.buildSUnitWithNumber(
                                    cursor.getInt(COL_SUNIT_NUMBER)
                            ));
                    startActivity(intent);
                }
            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUNIT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateSUnitList();
    }

    private void updateSUnitList() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Sort order:  Ascending, by date.
        String sortOrder = DroidCavationContract.SUnitEntry.COLUMN_NUMBER + " ASC";
        Uri sUnitforIdUri = DroidCavationContract.SUnitEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                sUnitforIdUri,
                SUNIT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mSUnitListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSUnitListAdapter.swapCursor(null);
    }
}
