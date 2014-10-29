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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.GeoCondition;
import au.edu.qut.inb348.muteme.model.Mute;


public class MuteGeoFragment extends Fragment implements MuteMapFragment.OnMapReadyListener {

    Mute mute;
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
        if (mute != null) {
            syncFromMute(mute);
        }
        return rootView;
    }

     GoogleMap muteMap;
    @Override
    public void onMapReady() {
        muteMap = mapFragment.getMap();


        if (muteMap != null) {
            muteMap.setPadding(5,5,5,5);

            LatLng mapLocation = null;
            if (mute.geoCondition.equals(GeoCondition.EVERYWHERE)){
                Location mostRecentLastKnownLocation = locationHelper.getMostRecentLastKnownLocation();
                if (mostRecentLastKnownLocation != null) {
                    mapLocation = new LatLng(mostRecentLastKnownLocation.getLatitude(), mostRecentLastKnownLocation.getLongitude());
                }
            }
            else {
                mapLocation = new LatLng(mute.geoCondition.latitude, mute.geoCondition.longitude);
            }

            if (mapLocation != null) {
                changeLocationTo(mapLocation);
                CameraUpdate cameraAtCurrentLocation =
                        CameraUpdateFactory.newLatLng(mapLocation);

                muteMap.moveCamera(cameraAtCurrentLocation);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                muteMap.animateCamera(zoom);
                muteMap.setOnMapClickListener( new GoogleMap.OnMapClickListener(){
                    @Override
                    public void onMapClick(LatLng latLng) {
                        changeLocationTo(latLng);
                    }
                });

            }
        }
    }
    Marker locationMarker;

    private void changeLocationTo(LatLng latLng) {
        if (locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = muteMap.addMarker(new MarkerOptions()
                .position(latLng));
        latitudeEdit.setText(latLng.latitude + "");
        longitudeEdit.setText(latLng.longitude + "");
    }

    public void syncFromMute(Mute item) {

        titleEdit.setText(item.title);
        latitudeEdit.setText(String.valueOf(item.geoCondition.latitude));
        longitudeEdit.setText(String.valueOf(item.geoCondition.longitude));
        radiusEdit.setText(String.valueOf(item.geoCondition.radiusMetres));
    }

    public void syncToMute(Mute item) {
        item.title = titleEdit.getText().toString();
        item.geoCondition.latitude = Double.parseDouble(latitudeEdit.getText().toString());
        item.geoCondition.longitude = Double.parseDouble(longitudeEdit.getText().toString());
        item.geoCondition.radiusMetres = Integer.parseInt(radiusEdit.getText().toString());
    }
}
