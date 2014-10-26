package au.edu.qut.inb348.muteme;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.Mute;

/**
 * A fragment representing a single Mute detail screen.
 * This fragment is either contained in a {@link MuteListActivity}
 * in two-pane mode (on tablets) or a {@link MuteDetailActivity}
 * on handsets.
 */
public class MuteDetailFragment extends Fragment implements MuteMapFragment.OnMapReadyListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Mute item;
    private MutesDbHelper dbHelper;
    private HashMap<DayOfWeek, Integer> toggleDayMap;
    private MuteRegistrar muteRegistrar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MuteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toggleDayMap = new HashMap<DayOfWeek, Integer>();
        toggleDayMap.put(DayOfWeek.MONDAY, R.id.mondayToggle);
        toggleDayMap.put(DayOfWeek.TUESDAY, R.id.tuesdayToggle);
        toggleDayMap.put(DayOfWeek.WEDNESDAY, R.id.wednesdayToggle);
        toggleDayMap.put(DayOfWeek.THURSDAY, R.id.thursdayToggle);
        toggleDayMap.put(DayOfWeek.FRIDAY, R.id.fridayToggle);
        toggleDayMap.put(DayOfWeek.SATURDAY, R.id.saturdayToggle);
        toggleDayMap.put(DayOfWeek.SUNDAY, R.id.sundayToggle);
        muteRegistrar = new MuteRegistrar(getActivity());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            item = dbHelper.getMute(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    EditText titleEdit;
    EditText latitudeEdit;
    EditText longitudeEdit;
    EditText radiusEdit;
    TimePicker startPicker;
    TimePicker stopPicker;
    View rootView;
    LocationHelper locationHelper;
    MuteMapFragment mapFragment;

    private void syncFromMute(Mute item) {

        titleEdit.setText(item.title);
        startPicker.setCurrentHour(item.chronoCondition.timeSpan.start.hour);
        startPicker.setCurrentMinute(item.chronoCondition.timeSpan.start.minutes);

        stopPicker.setCurrentHour(item.chronoCondition.timeSpan.stop.hour);
        stopPicker.setCurrentMinute(item.chronoCondition.timeSpan.stop.minutes);

        latitudeEdit.setText(String.valueOf(item.geoCondition.latitude));
        longitudeEdit.setText(String.valueOf(item.geoCondition.longitude));
        radiusEdit.setText(String.valueOf(item.geoCondition.radiusMetres));

        for(final DayOfWeek dayOfWeek : toggleDayMap.keySet()){
            ToggleButton toggle = (ToggleButton)rootView.findViewById(toggleDayMap.get(dayOfWeek));
            toggle.setChecked(item.chronoCondition.daysOfWeek.contains(dayOfWeek));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mute_detail, container, false);
        titleEdit = (EditText)rootView.findViewById(R.id.titleEdit);
        startPicker = (TimePicker)rootView.findViewById(R.id.startPicker);
        stopPicker = (TimePicker)rootView.findViewById(R.id.stopPicker);
        latitudeEdit = (EditText)rootView.findViewById(R.id.latitudeEdit);
        longitudeEdit = (EditText)rootView.findViewById(R.id.longitudeEdit);
        radiusEdit = (EditText)rootView.findViewById(R.id.radiusEdit);
        startPicker.setIs24HourView(true);
        stopPicker.setIs24HourView(true);
        mapFragment =  new MuteMapFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.mute_map_container, mapFragment)
                .commit();
        locationHelper = new LocationHelper(getActivity());

        if (item != null) {
            syncFromMute(item);
        }

        return rootView;
    }
    @Override
    public void onPause() {
        muteRegistrar.deregister(item);
        syncToMute(item);
        muteRegistrar.register(item);
        dbHelper.updateMute(item);

        super.onPause();
    }

    private void syncToMute(Mute item) {
        item.title = titleEdit.getText().toString();
        item.chronoCondition.timeSpan.start.hour = startPicker.getCurrentHour();
        item.chronoCondition.timeSpan.start.minutes = startPicker.getCurrentMinute();
        item.chronoCondition.timeSpan.stop.hour = stopPicker.getCurrentHour();
        item.chronoCondition.timeSpan.stop.minutes = stopPicker.getCurrentMinute();
        item.geoCondition.latitude = Double.parseDouble(latitudeEdit.getText().toString());
        item.geoCondition.longitude = Double.parseDouble(longitudeEdit.getText().toString());
        item.geoCondition.radiusMetres = Integer.parseInt(radiusEdit.getText().toString());
        for(final DayOfWeek dayOfWeek : toggleDayMap.keySet()){
            ToggleButton toggle = (ToggleButton)rootView.findViewById(toggleDayMap.get(dayOfWeek));
            if (toggle.isChecked()) {
                item.chronoCondition.daysOfWeek.add(dayOfWeek);
            }else {
                item.chronoCondition.daysOfWeek.remove(dayOfWeek);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        dbHelper = new MutesDbHelper(getActivity());
        super.onAttach(activity);
    }

    @Override
    public void onMapReady() {
        final GoogleMap muteMap = mapFragment.getMap();


        if (muteMap != null) {
            muteMap.setPadding(5,5,5,5);
            Location currentLocation = locationHelper.getMostRecentLastKnownLocation();
            CameraUpdate cameraAtCurrentLocation =
                    CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
            muteMap.moveCamera(cameraAtCurrentLocation);
            muteMap.animateCamera(zoom);
            muteMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    latitudeEdit.setText(latLng.latitude + "");
                    longitudeEdit.setText(latLng.latitude + "");
                }
            });
        }
    }

}
