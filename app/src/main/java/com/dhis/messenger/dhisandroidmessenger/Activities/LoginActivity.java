package com.dhis.messenger.dhisandroidmessenger.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dhis.messenger.dhisandroidmessenger.R;
import com.dhis.messenger.dhisandroidmessenger.Rest.RestLogin;

/**
 * Created by yrjanaff on 16.03.2016.
 */
public class LoginActivity extends Activity{

    EditText server;
    EditText username;
    EditText password;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        server = (EditText) findViewById(R.id.serverInput);
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.signinButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Hei. Button pressed!");
                logIn(v);
            }
        });
        System.out.println("\nTest!!!");
    }

    public void logIn(View view)
    {
        System.out.println("INNI LOGIN!!!!!");
        String serverString = server.getText().toString();
        String userString = username.getText().toString();
        String pasString = password.getText().toString();

        serverString = "http://193.157.213.241:8080/api/me";

        String formatCredentials = String.format("%s:%s", userString, pasString);
        String server = serverString + (serverString.endsWith("/") ? "" : "/");
        String credentials = Base64.encodeToString(formatCredentials.getBytes(), Base64.NO_WRAP);

        RestLogin restLogin = new RestLogin();
        String response = restLogin.DHISLogin(server, credentials);

        System.out.println("\n\nResponse: " + response);
    }
}
