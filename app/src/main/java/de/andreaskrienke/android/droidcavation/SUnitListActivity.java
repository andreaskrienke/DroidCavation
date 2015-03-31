package de.andreaskrienke.android.droidcavation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * SUNIT List Activity
 */
public class SUnitListActivity extends ActionBarActivity implements SUnitListActivityFragment.Callback {

    private static final String LOG_TAG = SUnitListActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    private Uri selectedItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunit_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_sunit_detail) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            if (savedInstanceState == null) {
                //getSupportFragmentManager().beginTransaction()
                //        .add(R.id.fragment_sunit_list, new SUnitListActivityFragment())
                //        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_sunit_detail, new SUnitDetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        SUnitListActivityFragment sUnitListActivityFragment =  ((SUnitListActivityFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_sunit_list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sunit_list, menu);
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

        if (id == R.id.action_sunit_add) {
            startActivity(new Intent(this, SUnitDetailActivity.class));
            return true;
        }

        if (id == R.id.action_sunit_edit) {

            if (selectedItemUri != null) {


                Bundle arguments = new Bundle();
                arguments.putParcelable(SUnitDetailActivityFragment.DETAIL_URI, selectedItemUri);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SUnitDetailEditFragment editFragment = new SUnitDetailEditFragment();
                editFragment.setArguments(arguments);

                fragmentTransaction
                        .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_sunit_detail))
                        .add(R.id.fragment_sunit_detail, editFragment)
                        .commit();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri) {

        selectedItemUri = contentUri;

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(SUnitDetailActivityFragment.DETAIL_URI, contentUri);

            SUnitDetailActivityFragment fragment = new SUnitDetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_sunit_detail, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, SUnitDetailActivity.class).setData(contentUri);
            startActivity(intent);
        }

    }

}
