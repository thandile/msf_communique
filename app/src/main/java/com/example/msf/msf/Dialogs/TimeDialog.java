package com.example.msf.msf.Dialogs;

/**
 * Created by Thandile on 2016/09/22.
 * class that creates the time picker
 */

import java.util.Calendar;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static EditText txt_time;

    public static TimeDialog newInstance(View view){
        txt_time=(EditText)view;
        return(new TimeDialog());
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default time in the dialog
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = 0;
                //c.get(Calendar.MINUTE);
        System.out.println("This is the minute "+ minute);
       // Log.d(TAG, )
        //Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,false);

    }


    public void onTimeSet(TimePicker picker,int hour, int minute){
        String min;
        String hr;
        if (minute<10) {
           // txt_time.setText(hour + ":0" + minute);
            min = "0"+minute;
        }
        else{
            min = ""+minute;
        }
        if (hour<10){
             hr = "0"+hour;
        }
        else {
            hr = ""+hour;
        }
        //else
       // {
            txt_time.setText(hr + ":" + min);
      //  }
    }
}