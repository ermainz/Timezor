package com.ermainz.timezor;

//import android.support.v4.app.Fragment;

/**
 * Created by kire on 6/10/13.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    Context mContext;
    Vibrator mVibrator;

    SaveTimerListener saveTimerCallback;
    FragmentManager fm;

    public TimerRunningFragment(Context context, MainActivity.TimerPageFragmentListener listener, Long time, FragmentManager fragmentManager){
        mListener = listener;
        timeToRun = time;
        mContext = context;
        fm = fragmentManager;
    }

    public interface SaveTimerListener {
        public void saveTimer(long seconds, String name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer_updater.cancel(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_running, container, false);

        View.OnClickListener stopClickListener = new View.OnClickListener() {
            public void onClick(View v){
                mListener.onStopTimer();
                timer_updater.cancel(true);
                if(mVibrator != null){
                    mVibrator.cancel();
                }
            }
        };

        View.OnClickListener saveClickListener = new View.OnClickListener() {
            public void onClick(View v){
                SaveTimerNameDialog dialog = new SaveTimerNameDialog(saveTimerCallback, timeToRun);
                dialog.show(fm, "DialogFragment");
            }
        };

        view.findViewById(R.id.stop_button).setOnClickListener(stopClickListener);
        view.findViewById(R.id.save_button).setOnClickListener(saveClickListener);

        hour2_view = (TextView) view.findViewById(R.id.hour_text2_running);
        hour1_view = (TextView) view.findViewById(R.id.hour_text1_running);
        min2_view = (TextView) view.findViewById(R.id.min_text2_running);
        min1_view = (TextView) view.findViewById(R.id.min_text1_running);
        sec2_view = (TextView) view.findViewById(R.id.sec_text2_running);
        sec1_view = (TextView) view.findViewById(R.id.sec_text1_running);

        timer_updater = new RefreshTimerText(mContext, timeToRun).execute();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            saveTimerCallback = (SaveTimerListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement SaveTimerListener interface");
        }
    }

    public void updateTimeText(Long millisecondsLeft){

        if (millisecondsLeft < 0){
            hour2_view.setText(Long.toString(0));
            hour1_view.setText(Long.toString(0));
            min2_view.setText(Long.toString(0));
            min1_view.setText(Long.toString(0));
            sec2_view.setText(Long.toString(0));
            sec1_view.setText(Long.toString(0));
            return;
        }
        long secondsLeft = millisecondsLeft / 1000;
        long hours = secondsLeft/60/60;
        long minutes = (secondsLeft - hours*60*60) / 60;
        long seconds = secondsLeft - minutes*60 - hours*60*60;

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

    public void notifyTimerFinished(){
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 2000, 2000, 1000};
        mVibrator.vibrate(pattern, -1);
    }

    protected class RefreshTimerText extends AsyncTask {

        long endTime;
        long remainingTime;
        Context mContext;

        protected RefreshTimerText(Context context, Long initialAmountSec){
            remainingTime = initialAmountSec * 1000;
            updateTimeText(remainingTime);
            mContext = context;
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
                        if(mVibrator != null){
                            mVibrator.cancel();
                        }
                        return null;
                    }
                    Thread.sleep(1000);
                    publishProgress();
                } catch (InterruptedException e){
                    e.printStackTrace();
                };
            }
            notifyTimerFinished();
            return null;
        }

    }


}
