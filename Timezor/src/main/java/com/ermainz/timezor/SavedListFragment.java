package com.ermainz.timezor;

/**
 * Created by kire on 6/13/13.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SavedListFragment extends Fragment {

    ListView mListView;
    SavedTimerListAdapter mListAdapter;
    List<Timer> mTimerList;
    MainActivity.TimerPageFragmentListener mListener;

    SavedListFragment(List<Timer> timerList, MainActivity.TimerPageFragmentListener listener){
        mTimerList = timerList;
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_list, container, false);

        mListView = (ListView) view.findViewById(R.id.savedTimerListView);
        mListAdapter = new SavedTimerListAdapter(inflater, mTimerList, mListener);
        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Timer timer = mListAdapter.getItem(i);
            }
        };
        //mListView.setOnItemClickListener(itemClickListener);

        return view;
    }

    public void addNewData(Timer newTimer){
        mListAdapter.notifyDataSetChanged();
    }

}
