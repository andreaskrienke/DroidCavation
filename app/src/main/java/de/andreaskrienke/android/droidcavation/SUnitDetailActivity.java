package de.andreaskrienke.android.droidcavation;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SUnitDetailActivity extends ActionBarActivity {

    private static final String LOG_TAG = SUnitDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunit_detail);
        if (savedInstanceState == null) {

            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_sunit_detail);
            if (frag != null) {
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
            }

            Uri uri = getIntent().getData();

            if (uri != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_sunit_detail, new SUnitDetailActivityFragment())
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
//                        .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_sunit_detail))
                        .add(R.id.fragment_sunit_detail, new SUnitDetailEditFragment())
                        .commit();
            }
        }

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

        if (id == R.id.action_sunit_edit) {

            //startActivity(new Intent(this, SUnitDetailEditFragment.class));
            //return true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SUnitDetailEditFragment editFragment = new SUnitDetailEditFragment();


            fragmentTransaction
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_sunit_detail))
                    .add(R.id.fragment_sunit_detail, editFragment)
                    .commit();

//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

        }
        return super.onOptionsItemSelected(item);
    }

}
