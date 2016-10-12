package com.example.msf.msf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
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
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Deserializers.Users;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.PatientsDeserialiser;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Outcomes.CreateOutcomeFragment;
import com.squareup.otto.Subscribe;

import java.io.File;
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
    public static final String SERVER_URL =  "https://drtbdemo.herokuapp.com/api/";
    public static final String MyPREFERENCES = "MyLogin" ;
    public static String Username = "usernameKey";
    public static String Password = "passwordKey";
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

            final String pass = sharedpreferences.getString(Password, null);
            final String uname = sharedpreferences.getString(Username, null);
           // Toast.makeText(LoginActivity.this, uname + " " + pass, Toast.LENGTH_SHORT).show();
            usernameET.setText(uname);
            pwdET.setText(pass);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();

                username = usernameET.getText().toString();
                // Get Password Edit View Value
                password = pwdET.getText().toString();

                if (uname != null) {
                    String pass = sharedpreferences.getString(Password, null);
                    String uname = sharedpreferences.getString(Username, null);
                    //Toast.makeText(LoginActivity.this, uname + " " + pass, Toast.LENGTH_SHORT).show();
                    if (pass.equals(password) && uname.equals(username)){
                      //  Toast.makeText(LoginActivity.this, "correct",
                        //        Toast.LENGTH_SHORT).show();
                        navigateToHomeActivity();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Incorrect username/password.",
                                Toast.LENGTH_SHORT).show();
                    }
               }
                else {
                    usersGet();
                }

            }
        });
    }

    public boolean fileExistance(String FILENAME){
        File file = LoginActivity.this.getApplicationContext().getFileStreamPath(FILENAME);
        return file.exists();
    }

    public void usersGet() {
        Interface communicatorInterface = Auth.getInterface(username, password);
        Callback<List<Users>> callback = new Callback<List<Users>>() {
            @Override
            public void success(List<Users> serverResponse, Response response2) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Username, usernameET.getText().toString());
                editor.putString(Password, pwdET.getText().toString());
                editor.commit();
                navigateToHomeActivity();
                prgDialog.hide();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                   // prgDialog.hide();
                    //Toast.makeText(getApplicationContext(), "Incorrect username/password", Toast.LENGTH_LONG).show();
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

    @Override
    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){


    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        prgDialog.hide();
        Toast.makeText(LoginActivity.this, "error   " +
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
