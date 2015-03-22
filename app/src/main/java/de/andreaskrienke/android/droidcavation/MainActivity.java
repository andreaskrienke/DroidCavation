package de.andreaskrienke.android.droidcavation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void initListeners() {
        //our activity is click listener for btn_start_sunits
        findViewById(R.id.btn_start_sunits).setOnClickListener(this);
        //our activity is click listener for btn_start_features
        findViewById(R.id.btn_start_features).setOnClickListener(this);
        //our activity is click listener for btn_start_projects
        findViewById(R.id.btn_start_projects).setOnClickListener(this);
        //our activity is click listener for btn_start_sites
        findViewById(R.id.btn_start_sites).setOnClickListener(this);
        //our activity is click listener for btn_start_squares
        findViewById(R.id.btn_start_squares).setOnClickListener(this);
        //our activity is click listener for btn_start_media
        findViewById(R.id.btn_start_media).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this, ((Button) v).getText(), Toast.LENGTH_SHORT).show();

        if (v.getId() == R.id.btn_start_sunits) {
            Intent intent = new Intent(this, SUnitListActivity.class);
            startActivity(intent);
        }
    }

}
