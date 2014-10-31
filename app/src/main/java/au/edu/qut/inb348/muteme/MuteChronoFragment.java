package au.edu.qut.inb348.muteme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import java.util.HashMap;
import au.edu.qut.inb348.muteme.model.DayOfWeek;
import au.edu.qut.inb348.muteme.model.Mute;

/*
    Written by Chong Lu.
 */
public class MuteChronoFragment extends Fragment {

    private HashMap<DayOfWeek, Integer> toggleDayMap;
    Mute mute;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mute_chrono, container, false);
        startPicker = (TimePicker)rootView.findViewById(R.id.startPicker);
        stopPicker = (TimePicker)rootView.findViewById(R.id.stopPicker);
        startPicker.setIs24HourView(true);
        stopPicker.setIs24HourView(true);
        if (mute != null) {
            syncFromMute(mute);
        }
        return rootView;
    }

    public void syncToMute(Mute item) {
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
    public void syncFromMute(Mute item) {
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
