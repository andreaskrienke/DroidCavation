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
import android.widget.TextView;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;


/**
 * a placeholder fragment containing a simple view.
 */
public class SUnitDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SUnitDetailActivityFragment.class.getSimpleName();

    private static final int SUNIT_DETAIL_LOADER = 0;

    static final String DETAIL_URI = "URI";

    private Uri mUri;

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

        // The detail Activity called via intent.
//        Intent intent = getActivity().getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            String sUnitStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//            ((TextView) rootView.findViewById(R.id.edit_sunit_number))
//                    .setText(sUnitStr);
//        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SUNIT_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        Uri sUnitforIdUri = intent.getData();

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
        */
        Log.v(LOG_TAG, "In onCreateLoader");
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    SUNIT_COLUMNS,
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
        int id = data.getInt(COL_SUNIT_ID);

        TextView detailSUnitIdTextView = (TextView)getView().findViewById(R.id.sunit_id);
        detailSUnitIdTextView.setText(String.valueOf(id));

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
}
