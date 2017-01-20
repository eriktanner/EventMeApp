package com.eventmeapp.eventmeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Erik Fok on 10/26/2016.
 */

/*Class Registration Activity*/
public class LoginRegisterScreen extends Activity {

    ConnectToServer connectionClass;
    NewUserRequest newUserRequestClass;
    EditText etUsername, etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;
    Button bRegister;
    ProgressBar pbBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acivity_registration);

        /**Set Up Buttons**/
        connectionClass = new ConnectToServer();
        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        bRegister = (Button) findViewById(R.id.regBut);
        pbBar = (ProgressBar) findViewById(R.id.progressBarRegister);
        pbBar.setVisibility(View.GONE);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().compareTo(etConfirmPassword.getText().toString()) != 0) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password must match.", Toast.LENGTH_SHORT).show();
                } else if (etFirstName != null && etLastName != null && etEmail != null && etPassword != null && etConfirmPassword != null) {
                    newUserRequestClass = new NewUserRequest();// this is the Asynctask
                    newUserRequestClass.execute("");
                } else {
                    Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_SHORT).show();
                }
            }
        }); //End of OnClickListener


    } //End of onCreate()


    public class NewUserRequest extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        @Override
        protected void onPreExecute() {
            pbBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbBar.setVisibility(View.GONE);
            Toast.makeText(LoginRegisterScreen.this, r, Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent mainIntent = new Intent(LoginRegisterScreen.this, LoginScreen.class);
                LoginRegisterScreen.this.startActivity(mainIntent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with server";
                } else {
                    String query = "INSERT INTO users (FirstName,LastName,UserEmail,Password)\n" +
                            "VALUES ('" + firstName + "','" + lastName+ "','" + email + "','" + password+"')";
                    PreparedStatement ps = con.prepareStatement(query);
                    int rs = ps.executeUpdate();

                    if(rs >= 0) {
                        z = "Registration Successful";
                        isSuccess=true;
                    } else {
                        z = "Registration Failed. Try new values.";
                        isSuccess = false;
                    }

                    z = "Registration Successful";
                }
            } catch (Exception ex) {
                z = "Error" + ex.getMessage();
            }
            return z;
        }

    }/*End of NewUserRequest*/





}
