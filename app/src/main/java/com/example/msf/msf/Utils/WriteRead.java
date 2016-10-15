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


    public static void createDir(String dirName, String fileName, String text, Context ctx){
        if (fileExistance(fileName,ctx)){
            //Log.d()
        }
        File mydir = ctx.getDir(dirName, Context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, fileName); //Getting a file within the dir.
        try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
            out.write(text.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
    private final String TAG = this.getClass().getSimpleName();
    public static void write(String fileName, String text, Context ctx) {
        //String PATIENTFILE = "my_file";
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

    public static boolean fileExistance(String FILENAME, Context ctx){
        File file = ctx.getFileStreamPath(FILENAME);
        return file.exists();
    }

}
