package com.example.newdatafetching;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;

public class TimerDialog extends AppCompatDialogFragment {
    private EditText editTime;
    int time = 0;
    Context mContext;

    /**
     * To get the context from the activities.
     * @param context
     */
    TimerDialog(Context context){
        mContext = context;
    }

    /**
     * Overrides the methods for the dialog.
     * One for OK button
     * One for cancel button
     * @param savedInstanceState
     * @return
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog .Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Set Timer")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        time = Integer.parseInt(editTime.getText().toString());
                        System.out.println(time);
                        setTimer(time);


                    }
                });
        editTime = view.findViewById(R.id.time_text);
        return builder.create();
    }

    public void setTimer(int time){
        AlarmTriggerBrodcastReciever counter = new AlarmTriggerBrodcastReciever(time*1000,1000,mContext);
        counter.start();

    }


}
