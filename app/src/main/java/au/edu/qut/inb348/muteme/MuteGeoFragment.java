package au.edu.qut.inb348.muteme;



import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.Mute;


public class MuteGeoFragment extends Fragment implements MuteMapFragment.OnMapReadyListener {

    private Mute item;
    private MutesDbHelper dbHelper;
    private MuteRegistrar muteRegistrar;

    EditText titleEdit;
    EditText latitudeEdit;
    EditText longitudeEdit;
    EditText radiusEdit;
    View rootView;
    LocationHelper locationHelper;
    MuteMapFragment mapFragment;
    public MuteGeoFragment() {
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        muteRegistrar = new MuteRegistrar(getActivity());
        if (getArguments().containsKey(MuteDetailActivity.ARG_ITEM_ID)) {
            item = dbHelper.getMute(getArguments().getLong(MuteDetailActivity.ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mute_geo, container, false);
        titleEdit = (EditText)rootView.findViewById(R.id.titleEdit);
        latitudeEdit = (EditText)rootView.findViewById(R.id.latitudeEdit);
        longitudeEdit = (EditText)rootView.findViewById(R.id.longitudeEdit);
        radiusEdit = (EditText)rootView.findViewById(R.id.radiusEdit);
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
                    CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
            muteMap.moveCamera(cameraAtCurrentLocation);
            muteMap.animateCamera(zoom);
            muteMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    latitudeEdit.setText(latLng.latitude + "");
                    longitudeEdit.setText(latLng.longitude + "");
                }
            });
        }
    }

    @Override
    public void onPause() {
        saveMute();
        super.onPause();
    }

    private void saveMute() {
        muteRegistrar.deregister(item);
        syncToMute(item);
        muteRegistrar.register(item);
        dbHelper.updateMute(item);
    }

    private void syncFromMute(Mute item) {

        titleEdit.setText(item.title);
        latitudeEdit.setText(String.valueOf(item.geoCondition.latitude));
        longitudeEdit.setText(String.valueOf(item.geoCondition.longitude));
        radiusEdit.setText(String.valueOf(item.geoCondition.radiusMetres));
    }

    private void syncToMute(Mute item) {
        item.title = titleEdit.getText().toString();
        item.geoCondition.latitude = Double.parseDouble(latitudeEdit.getText().toString());
        item.geoCondition.longitude = Double.parseDouble(longitudeEdit.getText().toString());
        item.geoCondition.radiusMetres = Integer.parseInt(radiusEdit.getText().toString());
    }
}
