package de.andreaskrienke.android.droidcavation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.andreaskrienke.android.droidcavation.data.DroidCavationContract;


public class SUnitDetailActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String LOG_TAG = SUnitDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunit_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_sunit_detail, new SUnitDetailActivityFragment())
                    .commit();
        }

        initListeners();
    }


    private void initListeners() {
        //our activity is click listener for btn_sunit_save
        Button btn = (Button)findViewById(R.id.btn_sunit_save);
        btn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sunit_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sunit_save) {
            long id = addSUnit(
                    Integer.getInteger(((EditText) findViewById(R.id.edit_sunit_number)).getText().toString()),
                    "short desc");

            Toast.makeText(getBaseContext(),
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
        Cursor sUnitCursor = getContentResolver().query(
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
            Uri insertedUri = getContentResolver().insert(
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
