package com.example.msf.msf;

import android.content.Context;
import android.test.AndroidTestCase;

import com.example.msf.msf.Fragments.Patient.PatientFragment;
import com.example.msf.msf.Utils.DataAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by Thandile on 2016/11/07.
 */

@RunWith(MockitoJUnitRunner.class)
public class DataAdapterTest extends AndroidTestCase{
    //DataAdapter dataAdapter;
    PatientFragment patientFragment;
    Context context;

    @Before
    public void setUp() throws Exception {
        //dataAdapter = new DataAdapter();
        patientFragment = mock(PatientFragment.class);
        context = patientFragment.getContext();
       // assertNotNull(context);

    }

    @Test
    public void getPatientInfo() throws Exception {
        long id = 4;
        //Context ctx = context;

        String patient = DataAdapter.getPatientInfo(id, context);

        String result = "Dion Philile Booi";

        assertEquals(patient, result);


    }

    @Test
    public void patientInfo() throws Exception {

    }

    @Test
    public void eventTypeGet() throws Exception {

    }

    @Test
    public void adverseEventGet() throws Exception {

    }

    @Test
    public void adverseEventsGet() throws Exception {

    }

    @Test
    public void ownersGet() throws Exception {

    }

    @Test
    public void usernames() throws Exception {

    }

    @Test
    public void sessionTypeLoad() throws Exception {

    }

    @Test
    public void sessionGet() throws Exception {

    }

    @Test
    public void loadPilots() throws Exception {

    }

    @Test
    public void loadOutcomes() throws Exception {

    }

    @Test
    public void subscriptionID() throws Exception {

    }

    @Test
    public void loadUserFromFile() throws Exception {

    }

    @Test
    public void getUserID() throws Exception {

    }

    @Test
    public void loadRecordTypeFromFile() throws Exception {

    }

    @Test
    public void loadFromFile() throws Exception {

    }

    @Test
    public void loadDrugs() throws Exception {

    }

}