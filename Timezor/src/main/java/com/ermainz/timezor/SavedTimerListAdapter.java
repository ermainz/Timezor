package com.ermainz.timezor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kire on 6/14/13.
 */
public class SavedTimerListAdapter extends BaseAdapter {

    List<Timer> data;

    LayoutInflater mInflater;
    MainActivity.TimerPageFragmentListener mListener;

    SavedTimerListAdapter(LayoutInflater inflater, List<Timer> timerList, MainActivity.TimerPageFragmentListener listener){
        mInflater = inflater;
        data = timerList;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View retView = view;
        if (retView == null){
            retView = mInflater.inflate(R.layout.saved_timer_row, viewGroup, false);
        }
        TextView timerLabelText = (TextView) retView.findViewById(R.id.timer_label);
        //TextView timerTimeText = (TextView) retView.findViewById(R.id.timer_seconds);

        Button deleteButton = (Button) retView.findViewById(R.id.delete_saved_button);
        Button startButton = (Button) retView.findViewById(R.id.start_saved_button);
        final int index = i;
        View.OnClickListener deleteClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                data.remove(index);
                notifyDataSetChanged();
            }
        };
        deleteButton.setOnClickListener(deleteClickListener);

        View.OnClickListener startClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.onStartSavedTimer((long) (data.get(index).seconds));
            }
        };
        startButton.setOnClickListener(startClickListener);

        timerLabelText.setText(data.get(i).label);
        //timerTimeText.setText(Long.toString(data.get(i).seconds));
        TextView hour2_view = (TextView) retView.findViewById(R.id.hour_text2_saved);
        TextView hour1_view = (TextView) retView.findViewById(R.id.hour_text1_saved);
        TextView min2_view = (TextView) retView.findViewById(R.id.min_text2_saved);
        TextView min1_view = (TextView) retView.findViewById(R.id.min_text1_saved);
        TextView sec2_view = (TextView) retView.findViewById(R.id.sec_text2_saved);
        TextView sec1_view = (TextView) retView.findViewById(R.id.sec_text1_saved);
        long time = data.get(i).seconds;
        long hours = time/60/60;
        long minutes = (time - hours*60*60) / 60;
        long seconds = time - minutes*60 - hours*60*60;

        if(hours<10){
            hour2_view.setText(Long.toString(0));
            hour1_view.setText(Long.toString(hours));
        } else {
            hour2_view.setText(Long.toString(hours/10));
            hour1_view.setText(Long.toString( hours-((hours/10)*10) ));
        }
        if(minutes<10){
            min2_view.setText(Long.toString(0));
            min1_view.setText(Long.toString(minutes));
        } else {
            min2_view.setText(Long.toString(minutes/10));
            min1_view.setText(Long.toString( minutes-((minutes/10)*10) ));
        }
        if(seconds<10){
            sec2_view.setText(Long.toString(0));
            sec1_view.setText(Long.toString(seconds));
        } else {
            sec2_view.setText(Long.toString(seconds/10));
            sec1_view.setText(Long.toString( seconds-((seconds/10)*10) ));
        }

        return retView;
    }
}
