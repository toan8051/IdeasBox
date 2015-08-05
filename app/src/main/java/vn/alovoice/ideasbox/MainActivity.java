package vn.alovoice.ideasbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by VIENTHONG on 8/1/2015.
 */
public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    // Called to lazily initialize the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu items to the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // Called every time user clicks on an action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_send:
                startActivity(new Intent(this,IdeaActivity.class));
                return true;
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class)); ;
                return true;
            default:
                return false;
        }
    }
}
