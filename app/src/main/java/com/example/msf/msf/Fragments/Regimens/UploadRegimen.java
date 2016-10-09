package com.example.msf.msf.Fragments.Regimens;

import android.content.Context;
import android.util.Log;

import com.example.msf.msf.API.Communicator;
import com.example.msf.msf.Utils.WriteRead;

/**
 * Created by Thandile on 2016/10/09.
 */

public class UploadRegimen {

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
    }
}
