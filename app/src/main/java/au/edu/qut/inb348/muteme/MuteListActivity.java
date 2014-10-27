package au.edu.qut.inb348.muteme;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.ChronoCondition;
import au.edu.qut.inb348.muteme.model.GeoCondition;
import au.edu.qut.inb348.muteme.model.Mute;
import au.edu.qut.inb348.muteme.model.TimeOfDay;
import au.edu.qut.inb348.muteme.model.TimeSpan;


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
    public void onItemSelected(long id) {
        Intent detailIntent = new Intent(this, MuteDetailActivity.class);
        detailIntent.putExtra(MuteDetailActivity.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }

}
