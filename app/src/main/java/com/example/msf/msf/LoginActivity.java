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
import com.example.msf.msf.API.BusProvider;
import com.example.msf.msf.API.Models.Users;
import com.example.msf.msf.API.ErrorEvent;
import com.example.msf.msf.API.Interface;
import com.example.msf.msf.API.ServerEvent;
import com.example.msf.msf.Fragments.Notfications.NotificationFragment;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity{
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
    String uname;
    String pass;
    private final String TAG = this.getClass().getSimpleName();


    public static final String MyPREFERENCES = "MyLogin" ;
    public static String Username = "usernameKey";
    public static String Password = "passwordKey";
    SharedPreferences sharedpreferences;
    String menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        menuFragment = getIntent().getStringExtra("menuFragment");
        Log.d(TAG,"menuFragment "+ menuFragment);
        errorMsg = (TextView)findViewById(R.id.login_error);
        usernameET = (EditText)findViewById(R.id.loginEmail);
        pwdET = (EditText)findViewById(R.id.loginPassword);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        login = (Button) findViewById(R.id.btnLogin);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        pass = sharedpreferences.getString(Password, null);
        uname = sharedpreferences.getString(Username, null);
        usernameET.setText(uname);
        pwdET.setText(pass);
        loginListener();
    }

    private void loginListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prgDialog.show();
                username = usernameET.getText().toString();
                password = pwdET.getText().toString();
                if (uname != null) {
                    String pass = sharedpreferences.getString(Password, null);
                    String uname = sharedpreferences.getString(Username, null);
                    if (pass.equals(password) && uname.equals(username)){
                        navigateToHomeActivity();
                    }
                    else {
                        prgDialog.hide();
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
        if (menuFragment != null){
            homeIntent.putExtra("menuFragment", menuFragment);
        }
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
        int id = item.getItemId();
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
        Toast.makeText(LoginActivity.this,
                errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

}
