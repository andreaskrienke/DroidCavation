package de.andreaskrienke.android.droidcavation.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Date Chooser class for View objects
 */
public class DateChooser implements View.OnFocusChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView textView;
    private Calendar myCalendar;
    private Context ctx;

    public DateChooser(TextView textView, Context ctx){
        this.textView = textView;
        this.textView.setOnFocusChangeListener(this);
        this.textView.setOnClickListener(this);
        this.ctx = ctx;
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        textView.setText(Utility.sdformat.format(myCalendar.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        new DatePickerDialog(ctx, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}