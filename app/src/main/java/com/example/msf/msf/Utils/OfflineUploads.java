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
                Log.i("NET", "drugsReplace with 1 " + drugsReplace);
                drugIDs[0] = drugsReplace;
            }
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.regimenPost(input[0], input[1], drugIDs, input[3], input[4]);
        }
        context.deleteFile("regimenPost");
    }

    public static void regimenUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("regimenUpdate", context)) {
            String file = WriteRead.read("regimenUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            long[] drugIDs = new long[input[2].length()];
            String drugsReplace = input[2].replace("[", "").replace("]", "");
            if (drugsReplace.length() > 1) {
                String[] drugsSplit = input[2].replace("[", "").replace("]", "").split(", ");
                for (int i = 0; i < drugsSplit.length; i++) {
                    drugIDs[i] = Long.parseLong(drugsSplit[i].split(":")[0]);
                }
            } else {
                Log.i("NET", "drugsReplace with 1 " + drugsReplace);
                drugIDs[0] = Long.parseLong(drugsReplace);
            }
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.regimenUpdate(Long.parseLong(input[5]), input[0], input[1], drugIDs, input[3], input[4]);
        }
        context.deleteFile("regimenUpdate");
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

    public static void admissionUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("admissionUpdate", context)) {
            String file = WriteRead.read("admissionUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.admissionUpdate(Long.parseLong(input[5]), input[0], input[1], input[2], input[3], input[4]);
        }
        context.deleteFile("admissionUpdate");
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
        context.deleteFile("appointmentPost");
    }

    public static void appointmentUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("appointmentUpdate", context)) {
            String file = WriteRead.read("appointmentUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.appointmentUpdate(Long.parseLong(input[7]), input[0], input[1], input[2], input[3], input[4],
                    input[5], input[6]);
        }
        context.deleteFile("appointmentUpdate");
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

    public static void enrollmentUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("enrollmentUpdate", context)) {
            String file = WriteRead.read("enrollmentUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.enrollmentUpdate(Long.parseLong(input[4]), input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("enrollmentUpdate");
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

    public static void eventUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("eventUpdate", context)) {
            String file = WriteRead.read("eventUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");
            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.eventUpdate(Long.parseLong(input[4]), input[0], input[1], input[2], input[3], input[4]);
        }
        context.deleteFile("eventUpdate");
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

    public static void counsellingUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("counsellingUpdate", context)) {
            String file = WriteRead.read("counsellingUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.counsellingUpdate(Long.parseLong(input[3]),input[0], input[1], input[2]);
        }
        context.deleteFile("counsellingUpdate");
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
    public static void medicalReportUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("reportUpdate", context)) {
            String file = WriteRead.read("reportUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.reportUpdate(Long.parseLong(input[4]), input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("reportUpdate");
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

    public static void adverseEventUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("adverseEventUpdate", context)) {
            String file = WriteRead.read("adverseEventUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.adverseEventUpdate(Long.parseLong(input[4]), input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("adverseEventUpdate");
    }


    public static void patientOutcome(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("outcomePost", context)) {
            String file = WriteRead.read("outcomePost", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.outcomePost(input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("outcomePost");
    }

    public static void patientOutcomeUpdate(Context context) {
        Communicator communicator = new Communicator();
        if (WriteRead.fileExistance("OutcomeUpdate", context)) {
            String file = WriteRead.read("OutcomeUpdate", context);
            Log.i("NET", "file text " + file);
            String[] input = file.split("!");

            //Log.i("NET", drugIDs[0] + " " + drugIDs[1]);
            communicator.OutcomeUpdate(Long.parseLong(input[4]), input[0], input[1], input[2], input[3]);
        }
        context.deleteFile("OutcomeUpdate");
    }


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){

    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){

    }

}
