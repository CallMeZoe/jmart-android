package com.ahmadZufarJsmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadZufarJsmartMH.model.Account;
import com.ahmadZufarJsmartMH.request.LoginRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmail= findViewById(R.id.loginEmail);
        EditText edtPassword = findViewById(R.id.loginPass);
        Button btnLogin = findViewById(R.id.butLogin);
        TextView txtRegister = findViewById(R.id.butRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            JSONObject object = new JSONObject(response);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            loggedAccount = gson.fromJson(object.toString(), Account.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                };
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                LoginRequest loginRequest = new LoginRequest(email, password, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    public static Account getLoggedAccount(){
        return loggedAccount;
    }
}

//public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
//
//    private static final Gson gson = new Gson();
//    private static Account loggedAccount;
//    private EditText editEmail;
//    private EditText editPassword;
//    private Button btnLogin;
//    private TextView btnRegister;
//
//    public static Account getLoggedAccount(){
//        return loggedAccount;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        editEmail = findViewById(R.id.loginEmail);
//        editPassword = findViewById(R.id.loginPass);
//        btnLogin = findViewById(R.id.butLogin);
//        btnRegister = findViewById(R.id.butRegister);
//
//        btnLogin.setOnClickListener(this);
//        btnRegister.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if(v.getId()==R.id.butLogin){
//            String dataEmail = editEmail.getText().toString().trim();
//            String dataPassword = editPassword.getText().toString().trim();
//        }
//        else if(v.getId()==R.id.butRegister){
//            Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(moveIntent);
//        }
//    }
//
//    public void onResponse(String response) {
//        Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
//        try{
//            JSONObject jsonObject = new JSONObject(response);
//            moveIntent.putExtra("id", jsonObject.getInt("id"));
//        }catch (Exception e){
//            Toast.makeText(this, "Login Gagal:(", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Toast.makeText(this, "Login Berhasi:)", Toast.LENGTH_LONG).show();
//        startActivity(moveIntent);
//    }
//
//    public void onErrorResponse(VolleyError error) {
//        Toast.makeText(this, "Login Gagal:(", Toast.LENGTH_LONG).show();
//    }
//}