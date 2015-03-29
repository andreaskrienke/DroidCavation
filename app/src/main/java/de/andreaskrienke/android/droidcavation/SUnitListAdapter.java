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
        TextView numberView = (TextView) view.findViewById(R.id.list_item_sunit_number);
        numberView.setText(context.getResources().getString(R.string.sunit_list_number) + " " + number);

        // Read short desc from cursor
        String shortDesc = cursor.getString(SUnitListActivityFragment.COL_SUNIT_SHORT_DESC);
        if (shortDesc.length() > 0) {
            // Find TextView and set value
            TextView shortDescView = (TextView) view.findViewById(R.id.list_item_sunit_short_desc);
            shortDescView.setText(context.getResources().getString(R.string.sunit_list_short_desc) + " " + shortDesc);
        }

        /*
        // Read area from cursor
        String area = cursor.getString(SUnitListActivityFragment.COL_SUNIT_AREA_ID);
        // Find TextView and set value
        TextView txtAreaId = (TextView) view.findViewById(R.id.list_item_sunit_area_id);
        txtAreaId.setText(context.getResources().getString(R.string.sunit_list_area) + " " + area);

        // Read square from cursor
        String square = cursor.getString(SUnitListActivityFragment.COL_SUNIT_SQUARE_ID);
        // Find TextView and set value
        TextView txtSquareId = (TextView) view.findViewById(R.id.list_item_sunit_square_id);
        txtSquareId.setText(context.getResources().getString(R.string.sunit_list_square) + " " + square);

        // Read excavation date begin from cursor
        long excavationDateBegin = cursor.getLong(SUnitListActivityFragment.COL_SUNIT_EXCAVATION_DATE_BEGIN);
        if (excavationDateBegin > 0) {
            // Find TextView and set value
            TextView txtExcavationDateBegin = (TextView) view.findViewById(R.id.list_item_sunit_excavation_begin);
            txtExcavationDateBegin.setText(Utility.sdformat.format(Utility.loadDate(excavationDateBegin)));
        }
        // Read excavation date end from cursor
        long excavationDateEnd = cursor.getLong(SUnitListActivityFragment.COL_SUNIT_EXCAVATION_DATE_END);
        if (excavationDateEnd > 0) {
            // Find TextView and set value
            TextView txtExcavationDateEnd = (TextView) view.findViewById(R.id.list_item_sunit_excavation_end);
            txtExcavationDateEnd.setText(Utility.sdformat.format(Utility.loadDate(excavationDateEnd)));
        }
*/
    }
}
