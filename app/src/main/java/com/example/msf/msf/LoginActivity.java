package com.example.msf.msf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msf.msf.API.Auth;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    public static String username = null;
    public static String password = null;
    Context context = this;
    private final String TAG = this.getClass().getSimpleName();
    public static final String SERVER_URL =  "https://salty-tor-72502.herokuapp.com/api/";
    public static final String MyPREFERENCES = "MyLogin" ;
    public static final String Username = "usernameKey";
    public static final String Password = "passwordKey";
    SharedPreferences sharedpreferences;

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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();

                username = usernameET.getText().toString();
                // Get Password Edit View Value
                password = pwdET.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Username, usernameET.getText().toString());
                editor.putString(Password, pwdET.getText().toString());
                editor.commit();
                String pass = sharedpreferences.getString(Password, null);
                String uname = sharedpreferences.getString(Username, null);
                Toast.makeText(LoginActivity.this, uname+ " " + pass,Toast.LENGTH_LONG).show();
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
