package com.ermainz.timezor;

//import android.support.v4.app.Fragment;

/**
 * Created by kire on 6/10/13.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerRunningFragment extends Fragment {

    MainActivity.TimerPageFragmentListener mListener;

    TextView hour2_view;
    TextView hour1_view;
    TextView min2_view;
    TextView min1_view;
    TextView sec2_view;
    TextView sec1_view;

    Long timeToRun;

    AsyncTask timer_updater;

    public TimerRunningFragment(MainActivity.TimerPageFragmentListener listener, Long time){
        mListener = listener;
        timeToRun = time;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_running, container, false);

        View.OnClickListener stopClickListener = new View.OnClickListener() {
            public void onClick(View v){
                mListener.onSwitchTimerFragment(Long.valueOf("0"));
                timer_updater.cancel(true);
            }
        };

        view.findViewById(R.id.stop_button).setOnClickListener(stopClickListener);

        hour2_view = (TextView) view.findViewById(R.id.hour_text2_running);
        hour1_view = (TextView) view.findViewById(R.id.hour_text1_running);
        min2_view = (TextView) view.findViewById(R.id.min_text2_running);
        min1_view = (TextView) view.findViewById(R.id.min_text1_running);
        sec2_view = (TextView) view.findViewById(R.id.sec_text2_running);
        sec1_view = (TextView) view.findViewById(R.id.sec_text1_running);

        timer_updater = new RefreshTimerText(timeToRun).execute();

        return view;
    }

    public void updateTimeText(Long millisecondsLeft){
        long hours = millisecondsLeft/1000/60/60;
        long minutes = millisecondsLeft/1000/60 - hours*60;
        long seconds = millisecondsLeft/1000 - minutes*60;

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

    }

    protected class RefreshTimerText extends AsyncTask {

        long endTime;
        long remainingTime;

        protected RefreshTimerText(Long initialAmountSec){
            remainingTime = initialAmountSec * 1000;
            updateTimeText(remainingTime);
        }

        @Override
        protected void onProgressUpdate(Object... progress){
            super.onProgressUpdate(progress);

            long remainingTimeMilli = endTime - System.currentTimeMillis();
            remainingTime = remainingTimeMilli;
/*            String text = String.valueOf(remainingTimeMilli / 1000);
            time_text.setText(text);*/
            updateTimeText(remainingTimeMilli);
        }

        @Override
        protected Object doInBackground(Object... params){
            updateTimeText(remainingTime);
            endTime = System.currentTimeMillis() + remainingTime + 1000; //REDO
            while(remainingTime > 0){
                try {
                    if(isCancelled()){
                        break;
                    }
                    Thread.sleep(1000);
                    publishProgress();
                } catch (InterruptedException e){
                    e.printStackTrace();
                };
            }
            //TODO notify of finishing
            return null;
        }

    }


}
