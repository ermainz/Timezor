package com.ermainz.timezor;

/**
 * Created by kire on 6/5/13.
 */

//import android.app.FragmentManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerCreateFragment extends Fragment {

    /* timeValues[0] -> second
       timeValues[5] -> hour
     */
    int[] timeValues = new int[6];
    int timeValue = 0;
    TextView setTimeView;
    TextView hour2_view;
    TextView hour1_view;
    TextView min2_view;
    TextView min1_view;
    TextView sec2_view;
    TextView sec1_view;
    ViewGroup rootView;
    Bundle savedInstanceState_m;
    MainActivity.TimerPageFragmentListener mListener;

    public TimerCreateFragment(MainActivity.TimerPageFragmentListener listener){
        mListener = listener;
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    public TimerCreateFragment(){
        for(int i = 0; i < 6; i++){
            timeValues[i] = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.timer_create, container, false);
        hour2_view = (TextView) view.findViewById(R.id.hour_text2);
        hour1_view = (TextView) view.findViewById(R.id.hour_text1);
        min2_view = (TextView) view.findViewById(R.id.min_text2);
        min1_view = (TextView) view.findViewById(R.id.min_text1);
        sec2_view = (TextView) view.findViewById(R.id.sec_text2);
        sec1_view = (TextView) view.findViewById(R.id.sec_text1);
        savedInstanceState_m = savedInstanceState;


        View.OnClickListener startClickListener = new View.OnClickListener() {
            public void onClick(View v){
                int seconds = timeValues[0] + timeValues[1]*10;
                int minutes = timeValues[2] + timeValues[3]*10;
                int hours = timeValues[4] + timeValues[5]*10;
                int totalSeconds = seconds + minutes*60 + hours*60*60;
                mListener.onSwitchTimerFragment((long) (totalSeconds));
            }
        };

        View.OnClickListener deleteClickListener = new View.OnClickListener() {
            public void onClick(View v){
                for(int i = 0; i < 5; i++){
                    timeValues[i] = timeValues[i+1];
                }
                timeValues[5] = 0;
                hour2_view.setText(Integer.toString(timeValues[5]));
                hour1_view.setText(Integer.toString(timeValues[4]));
                min2_view.setText(Integer.toString(timeValues[3]));
                min1_view.setText(Integer.toString(timeValues[2]));
                sec2_view.setText(Integer.toString(timeValues[1]));
                sec1_view.setText(Integer.toString(timeValues[0]));
            }
        };

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            public void onClick(View v){

                switch (v.getId()) {
                    case R.id.button_0:
                        addValue(0);
                        break;
                    case R.id.button_1:
                        addValue(1);
                        break;
                    case R.id.button_2:
                        addValue(2);
                        break;
                    case R.id.button_3:
                        addValue(3);
                        break;
                    case R.id.button_4:
                        addValue(4);
                        break;
                    case R.id.button_5:
                        addValue(5);
                        break;
                    case R.id.button_6:
                        addValue(6);
                        break;
                    case R.id.button_7:
                        addValue(7);
                        break;
                    case R.id.button_8:
                        addValue(8);
                        break;
                    case R.id.button_9:
                        addValue(9);
                        break;
                }
            }
        };

        view.findViewById(R.id.button_0).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_1).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_2).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_3).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_4).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_5).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_6).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_7).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_8).setOnClickListener(numberClickListener);
        view.findViewById(R.id.button_9).setOnClickListener(numberClickListener);
        view.findViewById(R.id.backspace_button).setOnClickListener(deleteClickListener);
        view.findViewById(R.id.start_timer_button).setOnClickListener(startClickListener);

        rootView = view;
        return view;
    }

    public void addValue(int val){
        if (timeValues[5] == 0){
            for(int i = 5; i > 0; i--){
                timeValues[i] = timeValues[i-1];
            }
            timeValues[0] = val;
            hour2_view.setText(Integer.toString(timeValues[5]));
            hour1_view.setText(Integer.toString(timeValues[4]));
            min2_view.setText(Integer.toString(timeValues[3]));
            min1_view.setText(Integer.toString(timeValues[2]));
            sec2_view.setText(Integer.toString(timeValues[1]));
            sec1_view.setText(Integer.toString(timeValues[0]));
        }

    }


}