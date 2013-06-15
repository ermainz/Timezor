package com.ermainz.timezor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kire on 6/14/13.
 */
public class SaveTimerNameDialog extends DialogFragment {

    TimerRunningFragment.SaveTimerListener mSaveTimerCallback;
    Long mTimeToRun;

    SaveTimerNameDialog(TimerRunningFragment.SaveTimerListener saveTimerCallback, Long timeToRun){
        mSaveTimerCallback = saveTimerCallback;
        mTimeToRun = timeToRun;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.name_dialog, null);
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        TextView labelTextView = (TextView) view.findViewById(R.id.input_name);
                        mSaveTimerCallback.saveTimer(mTimeToRun, labelTextView.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
