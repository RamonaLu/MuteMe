package au.edu.qut.inb348.muteme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.List;
import java.util.zip.Inflater;

import au.edu.qut.inb348.muteme.model.ChronoCondition;
import au.edu.qut.inb348.muteme.model.Mute;

public class MutesAdapter extends ArrayAdapter<Mute>{

    int rowLayout;
    Context context;
    List<Mute> mutes;
    public MutesAdapter(Context context, int rowLayout, List<Mute> mutes) {
        super(context, rowLayout, mutes);
        this.rowLayout = rowLayout;
        this.context = context;
        this.mutes = mutes;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(rowLayout, null);
        }

        TextView line1 = (TextView)(view.findViewById(android.R.id.text1));
        Mute mute = mutes.get(position);
        line1.setText(mute.title);

        return view;
    }
}
