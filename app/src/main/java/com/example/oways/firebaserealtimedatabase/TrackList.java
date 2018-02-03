package com.example.oways.firebaserealtimedatabase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oways on 03-Feb-18.
 */

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> trackList;

    public TrackList(Activity context, List<Track> trackList) {
        super(context, R.layout.track_list_layout,trackList);
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.track_list_layout,null,true);
        TextView textViewName=(TextView)listViewItem.findViewById(R.id.textViewName);
        TextView textViewRating=(TextView)listViewItem.findViewById(R.id.textViewRating);

        Track track=trackList.get(position);
        textViewName.setText(track.getTrackName());
        textViewRating.setText(String.valueOf(track.getTrackRating()));

        return listViewItem;
    }
}
