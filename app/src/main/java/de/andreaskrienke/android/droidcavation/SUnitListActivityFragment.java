package de.andreaskrienke.android.droidcavation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SUnitListActivityFragment extends Fragment {

    private static final String LOG_TAG = SUnitListActivityFragment.class.getSimpleName();

    private ArrayAdapter<String> mSUnitListAdapter;

    public SUnitListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_sunit_list, container, false);


        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "String A",
                "String B",
                "String C"
        };

        List<String> sunitList = new ArrayList<String>(Arrays.asList(data));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mSUnitListAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_sunit, // The name of the layout ID.
                        R.id.list_item_sunit_textview, // The ID of the textview to populate.
                        sunitList);

        View rootView = inflater.inflate(R.layout.fragment_sunit_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_sunit);
        listView.setAdapter(mSUnitListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String sUnitStr = mSUnitListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SUnitDetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, sUnitStr);
                startActivity(intent);
            }
        });

        return rootView;

    }
}
