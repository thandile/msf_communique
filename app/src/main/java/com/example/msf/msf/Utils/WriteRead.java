package com.example.msf.msf.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.security.AccessController.getContext;

/**
 * Created by Thandile on 2016/09/22.
 */

public class WriteRead {


    private final String TAG = this.getClass().getSimpleName();
    public static void write(String fileName, String text, Context ctx) {
        //String PATIENTINFOFILE = "my_file";
        FileOutputStream fos;
        try {
            fos = ctx.openFileOutput(fileName, ctx.MODE_PRIVATE);
            fos.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String filename, Context ctx){

        try {

            FileInputStream fis = ctx.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
            //Log.d(TAG, sb.toString());
        } catch (IOException e) {
            return "";
        }
    }


}
