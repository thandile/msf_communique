package com.example.msf.msf.Utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Outcomes.CreateOutcomeFragment;
import com.example.msf.msf.Utils.WriteRead;
import com.squareup.otto.Subscribe;

import java.io.File;

/**
 * Created by Thandile on 2016/10/09.
 */

public class OfflineUploads {

    /**
     * uploads the regimen information captured offline
     * @param context
     */
    public static void regimen(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("regimenPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                String[] drugIDs = new String[input[2].length()];
                String drugsReplace = input[2].replace("[", "").replace("]", "");
                if (drugsReplace.length() > 1) {
                    String[] drugsSplit = input[2].replace("[", "").replace("]", "").split(", ");
                    for (int j = 0; j < drugsSplit.length; j++) {
                        drugIDs[j] = drugsSplit[j].split(":")[0];
                    }
                } else {
                    Log.i("NET", "drugsReplace with 1 " + drugsReplace);
                    drugIDs[0] = drugsReplace;
                }
                communicator.regimenPost(input[0], input[1], drugIDs, input[3], input[4]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the regimen information captured offline
     * @param context
     */
    public static void regimenUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("regimenUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                String[] drugIDs = new String[input[2].length()];
                String drugsReplace = input[2].replace("[", "").replace("]", "");
                if (drugsReplace.length() > 1) {
                    String[] drugsSplit = input[2].replace("[", "").replace("]", "").split(", ");
                    for (int j = 0; j < drugsSplit.length; j++) {
                        drugIDs[j] = drugsSplit[j].split(":")[0];
                    }
                } else {
                    Log.i("NET", "drugsReplace with 1 " + drugsReplace);
                    drugIDs[0] = drugsReplace;
                }
                communicator.regimenUpdate(Long.parseLong(input[5]), input[0], input[1], drugIDs, input[3], input[4]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the admission information captured offline
     * @param context
     */
    public static void admission(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("admissionPost")) {
                Log.i("NET", filesForUpload[i]);
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.admissionPost(input[0], input[1], input[2], input[3], input[4]);
                context.deleteFile("admissionPost");
            }
        }
    }

    /**
     * uploads the admission information captured offline
     * @param context
     */
    public static void admissionUpdate(Context context) {
        File mydir = context.getFilesDir();
        Log.i("NET", mydir.toString());
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++){
            if (filesForUpload[i].contains("admissionUpdate")) {
       // if (WriteRead.fileExistance("admissionUpdate", context)) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.admissionUpdate(Long.parseLong(input[5]), input[0], input[1], input[2],
                        input[3], input[4]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the appointment information captured offline
     * @param context
     */
    public static void appointment(Context context) {
        File mydir = context.getFilesDir();
        Log.i("NET", mydir.toString());
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++){
            if (filesForUpload[i].contains("appointmentPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", filesForUpload[i] + " " + file);
                String[] input = file.split("!");
                Log.i("NET", "" + input.length);
                if (input.length == 7) {
                    communicator.appointmentPost(input[0], input[1], input[2], input[3], input[4],
                            input[5], input[6]);
                } else if (input.length == 6) {
                    communicator.appointmentPost(input[0], input[1], input[2], input[3], input[4],
                            input[5], "");
                } else {
                    communicator.appointmentPost(input[0], input[1], input[2], input[3], input[4],
                            "", "");
                }
                context.deleteFile(filesForUpload[i]);
            }
         }
    }

    /**
     * uploads the appointment information captured offline
     * @param context
     */
    public static void appointmentUpdate(Context context) {

        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("appointmentUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", filesForUpload[i] + " " + file);
                String[] input = file.split("!");
                Log.i("NET", "" + input.length);
                if (input.length == 7) {
                    communicator.appointmentUpdate(Long.parseLong(input[7]), input[0], input[1],
                            input[2], input[3], input[4],
                            input[5], input[6]);
                } else if (input.length == 6) {
                    communicator.appointmentUpdate(Long.parseLong(input[7]), input[0], input[1],
                            input[2], input[3], input[4],
                            input[5], "");
                } else {
                    communicator.appointmentUpdate(Long.parseLong(input[7]), input[0], input[1],
                            input[2], input[3], input[4],
                            "", "");
                }
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the enrollment information captured offline
     * @param context
     */
    public static void enrollment(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("enrollmentPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.enrollmentPost(input[0], input[1], input[2], input[3]);
                context.deleteFile("enrollmentPost");
            }
        }
    }

    /**
     * uploads the enrollment information captured offline
     * @param context
     */
    public static void enrollmentUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("enrollmentUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.enrollmentUpdate(Long.parseLong(input[4]), input[0], input[1],
                        input[2], input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the event information captured offline
     * @param context
     */
    public static void event(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("eventPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.eventPost(input[0], input[1], input[2], input[3], input[4]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the event information captured offline
     * @param context
     */
    public static void eventUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("eventUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.eventUpdate(Long.parseLong(input[4]), input[0], input[1], input[2],
                        input[3], input[4]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }


    /**
     * uploads the counselling information captured offline
     * @param context
     */
    public static void counselling(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("counsellingPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.counsellingPost(input[0], input[1], input[2]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the counselling information captured offline
     * @param context
     */
    public static void counsellingUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("counsellingUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.counsellingUpdate(Long.parseLong(input[3]), input[0], input[1], input[2]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the medical information captured offline
     * @param context
     */
    public static void medicalReport(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("reportPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.reportPost(input[0], input[1], input[2], input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the medical information captured offline
     * @param context
     */
    public static void medicalReportUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("reportUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.reportUpdate(Long.parseLong(input[4]), input[0], input[1], input[2],
                        input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the adverse event information captured offline
     * @param context
     */
    public static void adverseEvent(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            Log.i("NET", "before if");
            if (filesForUpload[i].contains("adverseEventPost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.adverseEventPost(input[0], input[1], input[2], input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the adverse event information captured offline
     * @param context
     */
    public static void adverseEventUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("adverseEventUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.adverseEventUpdate(Long.parseLong(input[4]), input[0], input[1],
                        input[2], input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }


    /**
     * uploads the patient outcome information captured offline
     * @param context
     */
    public static void patientOutcome(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("outcomePost")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.outcomePost(input[0], input[1], input[2], input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }

    /**
     * uploads the patient outcome information captured offline
     * @param context
     */
    public static void patientOutcomeUpdate(Context context) {
        File mydir = context.getFilesDir();
        String[] filesForUpload = mydir.list();
        Communicator communicator = new Communicator();
        for (int i = 0; i< filesForUpload.length; i++) {
            if (filesForUpload[i].contains("OutcomeUpdate")) {
                String file = WriteRead.read(filesForUpload[i], context);
                Log.i("NET", "file text " + file);
                String[] input = file.split("!");
                communicator.OutcomeUpdate(Long.parseLong(input[4]), input[0], input[1], input[2],
                        input[3]);
                context.deleteFile(filesForUpload[i]);
            }
        }
    }


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){

    }

}
