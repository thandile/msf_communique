package com.example.msf.msf;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.example.msf.msf.Utils.DataAdapter;

import org.junit.Before;
import org.junit.Test;


/**
 * Created by Thandile on 2016/11/07.
 */


public class DataAdapterInstrumentalTest extends ApplicationTestCase<Application> {
    //DataAdapter dataAdapter;
    Context context;

    public DataAdapterInstrumentalTest(Class applicationClass) {
        super(applicationClass);
    }

    @Before
    public void setUp() throws Exception {
        //dataAdapter = new DataAdapter();
        context = getContext();
        assertNotNull(context);

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