package au.edu.qut.inb348.muteme;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import au.edu.qut.inb348.muteme.data.MutesDbHelper;

/*
    Written by Chong Lu.
 */
public class MuteListActivity extends Activity
        implements MuteListFragment.Callbacks {

    MutesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mute_list);
        dbHelper = new MutesDbHelper(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_actions, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mute_cinemas:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            case R.id.action_mute_libraries:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemSelected(long id) {
        Intent detailIntent = new Intent(this, MuteDetailActivity.class);
        detailIntent.putExtra(MuteDetailActivity.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }

}
