package de.andreaskrienke.android.droidcavation;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class SUnitListActivity extends ActionBarActivity {

    private final String LOG_TAG = SUnitListActivity.class.getSimpleName();
    private static final String SHUNIT_LIST_FRAGMENT_TAG = "SULFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "in onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sunit_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sunit_detail_container, new SUnitListFragment(), SHUNIT_LIST_FRAGMENT_TAG)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sunits_list, menu);
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

//    /**
//     * Callback method from {@link SUnitListFragment.Callback}
//     * indicating that the item with the given ID was selected.
//     */
//    @Override
//    public void onItemSelected(Uri contentUri) {
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle args = new Bundle();
//            args.putParcelable(SUnitDetailFragment.DETAIL_URI, contentUri);
//
//            SUnitDetailFragment fragment = new SUnitDetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.sunit_detail_container, fragment, SHUNIT_LIST_FRAGMENT_TAG)
//                    .commit();
//        } else {
//            Intent intent = new Intent(this, SUnitDetailActivity.class).setData(contentUri);
//            startActivity(intent);
//        }
//    }
}
