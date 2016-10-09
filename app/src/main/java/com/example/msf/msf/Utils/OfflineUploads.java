package com.example.msf.msf.Utils;

import android.content.Context;
import android.util.Log;

import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.Utils.WriteRead;

/**
 * Created by Thandile on 2016/10/09.
 */

public class OfflineUploads {

    public static void regimen(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("regimenPost", context)) {
            String file = WriteRead.read("regimenPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            String[] drugIDs = new String[input[2].length()];
            String drugsReplace = input[2].replace("[", "").replace("]", "");
            if (drugsReplace.length() > 1) {
                String[] drugsSplit = input[2].replace("[", "").replace("]", "").split(", ");
                for (int i = 0; i < drugsSplit.length; i++) {
                    drugIDs[i] = drugsSplit[i].split(":")[0];
                }
            } else {
                Log.i("NET", "drugsreplace with 1 " + drugsReplace);
                drugIDs[0] = drugsReplace;
            }
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.regimenPost(input[0], input[1], drugIDs, input[3], input[4]);
        }
        context.deleteFile("regimenPost");
    }

    public static void admission(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("admissionPost", context)) {
            String file = WriteRead.read("admissionPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.admissionPost(input[0], input[1], input[2], input[3], input[4]);
        }
        context.deleteFile("admissionPost");
    }

    public static void appointment(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("appointmentPost", context)) {
            String file = WriteRead.read("appointmentPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.appointmentPost(input[0], input[1], input[2], input[3], input[4],
                    input[5], input[6]);
        }
        context.deleteFile("admissionPost");
    }

    public static void enrollment(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("enrollmentPost", context)) {
            String file = WriteRead.read("enrollmentPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.enrollmentPost(input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("enrollmentPost");
    }

    public static void event(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("eventPost", context)) {
            String file = WriteRead.read("eventPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.eventPost(input[0], input[1], input[2], input[3], input[4]);
        }
        context.deleteFile("eventPost");
    }

    public static void counselling(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("counsellingPost", context)) {
            String file = WriteRead.read("counsellingPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.counsellingPost(input[0], input[1], input[2]);
        }
        context.deleteFile("counsellingPost");
    }

    public static void medicalReport(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("reportPost", context)) {
            String file = WriteRead.read("reportPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.reportPost(input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("reportPost");
    }

    public static void adverseEvent(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("adverseEventPost", context)) {
            String file = WriteRead.read("adverseEventPost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.adverseEventPost(input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("adverseEventPost");
    }


}
