package com.example.msf.msf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Deserializers.PatientResponse;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.ServerEvent;
import com.squareup.otto.Produce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends AppCompatActivity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText usernameET;
    // Passwprd Edit View Object
    EditText pwdET;
    Button login;
    public static String username;
    public static String password;
    Context context = this;
    private final String TAG = this.getClass().getSimpleName();
    public static final String SERVER_URL =  "https://radiant-cliffs-27013.herokuapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find Error Msg Text View control by ID
        errorMsg = (TextView)findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        usernameET = (EditText)findViewById(R.id.loginEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText)findViewById(R.id.loginPassword);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                username = usernameET.getText().toString();
                // Get Password Edit View Value
                password = pwdET.getText().toString();
                usersGet();
            }
        });
    }
    public void usersGet() {
        final List<String> patientList = new ArrayList<String>();
        Interface communicatorInterface = Auth.getInterface();
        Callback<List<PatientsDeserialiser>> callback = new Callback<List<PatientsDeserialiser>>() {
            @Override
            public void success(List<PatientsDeserialiser> serverResponse, Response response2) {
                navigateToHomeActivity();
                prgDialog.hide();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    prgDialog.hide();
                    Toast.makeText(getApplicationContext(), "Incorrect username/password", Toast.LENGTH_LONG).show();
                    Log.e(TAG, error.getMessage());
                    error.printStackTrace();
                }
            }
        };
        communicatorInterface.getUsers(callback);

    }


    /**
     * Method gets triggered when Login button is clicked
     *
     * @param view
     */
    /**public void loginUser(View view){
        //navigateToHomeActivity();
        // Get Email Edit View Value
        username = usernameET.getText().toString();
        // Get Password Edit View Value
        password = pwdET.getText().toString();
        navigateToHomeActivity();
        // Instantiate Http Request Param Object
       // RequestParams params = new RequestParams();
        // Put Http parameter username with value of username Edit Value control
        params.put("username", username);
        // Put Http parameter password with value of Password Edit Value control
        params.put("password", password);
        CommuniqueAPI.getCredentials(username, password);
        // Invoke RESTful Web Service with Http parameters
        invokeWS(params, username, password);**/

   // }
/**
    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params, username, password

    public void invokeWS(RequestParams params, final String username, String password){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice
        CommuniqueAPI.get("login/"+username+"/", null, new AsyncHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                prgDialog.hide();
                navigateToHomeActivity();
            }


            // When the response returned by REST has Http response code other than '200'
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 403){
                    Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "There was a problem with your login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }**/



    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigateToHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
