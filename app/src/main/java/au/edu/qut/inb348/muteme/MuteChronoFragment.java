package au.edu.qut.inb348.muteme;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.HashMap;

import au.edu.qut.inb348.muteme.data.MutesDbHelper;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.Mute;

public class MuteChronoFragment extends Fragment {

    private HashMap<DayOfWeek, Integer> toggleDayMap;
    private MuteRegistrar muteRegistrar;

    private Mute item;
    private MutesDbHelper dbHelper;
    EditText titleEdit;
    TimePicker startPicker;
    TimePicker stopPicker;
    View rootView;

    public MuteChronoFragment() {
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

        if (getArguments().containsKey(MuteDetailActivity.ARG_ITEM_ID)) {
            item = dbHelper.getMute(getArguments().getLong(MuteDetailActivity.ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mute_chrono, container, false);
        titleEdit = (EditText)rootView.findViewById(R.id.titleEdit);
        startPicker = (TimePicker)rootView.findViewById(R.id.startPicker);
        stopPicker = (TimePicker)rootView.findViewById(R.id.stopPicker);
        startPicker.setIs24HourView(true);
        stopPicker.setIs24HourView(true);


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
    @Override
    public void onAttach(Activity activity) {
        dbHelper = new MutesDbHelper(getActivity());
        super.onAttach(activity);
    }

    private void syncToMute(Mute item) {
        item.title = titleEdit.getText().toString();
        item.chronoCondition.timeSpan.start.hour = startPicker.getCurrentHour();
        item.chronoCondition.timeSpan.start.minutes = startPicker.getCurrentMinute();
        item.chronoCondition.timeSpan.stop.hour = stopPicker.getCurrentHour();
        item.chronoCondition.timeSpan.stop.minutes = stopPicker.getCurrentMinute();
        for(final DayOfWeek dayOfWeek : toggleDayMap.keySet()){
            ToggleButton toggle = (ToggleButton)rootView.findViewById(toggleDayMap.get(dayOfWeek));
            if (toggle.isChecked()) {
                item.chronoCondition.daysOfWeek.add(dayOfWeek);
            }else {
                item.chronoCondition.daysOfWeek.remove(dayOfWeek);
            }
        }
    }
    private void syncFromMute(Mute item) {
        titleEdit.setText(item.title);
        startPicker.setCurrentHour(item.chronoCondition.timeSpan.start.hour);
        startPicker.setCurrentMinute(item.chronoCondition.timeSpan.start.minutes);
        stopPicker.setCurrentHour(item.chronoCondition.timeSpan.stop.hour);
        stopPicker.setCurrentMinute(item.chronoCondition.timeSpan.stop.minutes);

        for(final DayOfWeek dayOfWeek : toggleDayMap.keySet()){
            ToggleButton toggle = (ToggleButton)rootView.findViewById(toggleDayMap.get(dayOfWeek));
            toggle.setChecked(item.chronoCondition.daysOfWeek.contains(dayOfWeek));
        }
    }


}
