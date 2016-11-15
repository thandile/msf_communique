package com.example.msf.msf.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.security.AccessController.getContext;

/**
 * Created by Thandile on 2016/09/22.
 */

public class WriteRead {


    /**
     * writes a file to a specific directory in internal memory
     * @param dirName
     * @param fileName
     * @param text
     * @param ctx
     */
    public static void createDir(String dirName, String fileName, String text, Context ctx){
        if (fileExistance(fileName,ctx)){
        }
        File mydir = ctx.getDir(dirName, Context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, fileName); //Getting a file within the dir.
        fileWithinMyDir.setWritable(true);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileWithinMyDir);
            fos.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes files to internal memory
     * @param fileName
     * @param text
     * @param ctx
     */
    public static void write(String fileName, String text, Context ctx) {
        FileOutputStream fos;
        try {
            fos = ctx.openFileOutput(fileName, ctx.MODE_PRIVATE);
            fos.write(text.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads files that have been written to internal memory
     * @param filename
     * @param ctx
     * @return file contents
     */
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

    /**
     * determines if a file by a particular name exists in internal memory
     * @param FILENAME
     * @param ctx
     * @return true or false
     */
    public static boolean fileExistance(String FILENAME, Context ctx){
        File file = ctx.getFileStreamPath(FILENAME);
        return file.exists();
    }

}
