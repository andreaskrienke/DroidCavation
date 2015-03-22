package de.andreaskrienke.android.droidcavation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A fragment representing a single SUnit detail screen.
 * This fragment is either contained in a {@link SUnitListActivity}
 * in two-pane mode (on tablets) or a {@link SUnitDetailActivity}
 * on handsets.
 */
public class SUnitDetailFragment extends Fragment {

    static final String DETAIL_URI = "URI";

    private String mSUnitStr;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SUnitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sunit_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mSUnitStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_sunit_textview))
                    .setText(mSUnitStr);
        }

        return rootView;
    }
}
