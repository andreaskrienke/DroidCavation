package de.andreaskrienke.android.droidcavation;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by andy on 22.03.15.
 */
public class SUnitListAdapter extends CursorAdapter {

    public SUnitListAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_sunit, parent, false);

        return view;
    }

    /**
     *  This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Read number from cursor
        int number = cursor.getInt(SUnitListActivityFragment.COL_SUNIT_NUMBER);
        // Find TextView and set value
        TextView numberView = (TextView) view.findViewById(R.id.list_item_sunit_textview);
        numberView.setText(""+number);

        // Read short desc from cursor
        String shortDesc = cursor.getString(SUnitListActivityFragment.COL_SUNIT_SHORT_DESC);
        // Find TextView and set value
        TextView shortDescView = (TextView) view.findViewById(R.id.list_item_sunit_short_desc_textview);
        shortDescView.setText(shortDesc);

    }
}
