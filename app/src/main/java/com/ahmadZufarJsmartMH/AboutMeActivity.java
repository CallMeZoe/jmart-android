package com.ahmadZufarJsmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadZufarJsmartMH.model.Account;
import com.ahmadZufarJsmartMH.request.RegisterStoreRequest;
import com.ahmadZufarJsmartMH.request.RequestFactory;
import com.ahmadZufarJsmartMH.request.TopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Merupakan Class yang mengatur Activity About Me untuk tampilan aktivitas account
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class AboutMeActivity extends AppCompatActivity {

    private Button b1;
    private LinearLayout l2;
    private LinearLayout l3;
    private Button b2;
    private Button b3;
    private EditText edtTopup;
    private Button btnTopup;
    private Button btnInvoice;
    private Button btnRegisterStore;
    private Button btnCancelStore;
    private EditText NameStore;
    private EditText AddressStore;
    private EditText PhoneStore;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvBalance;
    private TextView tvHasilName;
    private TextView tvHasilAddress;
    private TextView tvHasilPhone;
    private static final Gson gson = new Gson();


    private Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        btnRegisterStore = findViewById(R.id.butRegRegisterStore);
        btnCancelStore = findViewById(R.id.butCancelRegisterStore);
        NameStore = findViewById(R.id.nameRegisterStore);
        AddressStore = findViewById(R.id.addressRegisterStore);
        PhoneStore = findViewById(R.id.phoneRegisterStore);
        edtTopup = findViewById(R.id.edtTopup);
        btnTopup = findViewById(R.id.butTopupAccountDetails);
        btnInvoice = findViewById(R.id.butGoInvoice);
        b1 = findViewById(R.id.butRegisterStore);
        l2 = findViewById(R.id.formRegisterStore);
        l3 = findViewById(R.id.hasilRegisterStore);
        tvName = findViewById(R.id.nameAccountDetails);
        tvEmail = findViewById(R.id.emailAccountDetails);
        tvBalance = findViewById(R.id.balanceAccountDetails);
        tvHasilName = findViewById(R.id.nameStore);
        tvHasilAddress = findViewById(R.id.addressStore);
        tvHasilPhone = findViewById(R.id.phoneStore);
        account = LoginActivity.getLoggedAccount();
        tvName.setText(account.name);
        tvEmail.setText(account.email);
        tvBalance.setText("" + account.balance);
        if (account.store != null){
            b1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.VISIBLE);
            tvHasilName.setText(account.store.name);
            tvHasilAddress.setText(account.store.address);
            tvHasilPhone.setText(account.store.phoneNumber);
        }
        else {
            b1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setVisibility(v.GONE);
                l2.setVisibility(v.VISIBLE);
                Toast.makeText(getApplicationContext(), "Register Store di click", Toast.LENGTH_SHORT).show();
            }
        });

        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(AboutMeActivity.this, InvoiceActivity.class);
                startActivity(moveIntent);
            }
        });

        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean object = Boolean.valueOf(response);
                        if(object){
                            refreshBalance();
                            edtTopup.getText().clear();
                            Toast.makeText(AboutMeActivity.this, "Topup Berhasil", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AboutMeActivity.this, "Topup Gagal", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Topup Gagal", Toast.LENGTH_SHORT).show();
                    }
                };
                account = LoginActivity.getLoggedAccount();
                double balance = Double.valueOf(edtTopup.getText().toString());
                TopUpRequest topupRequest = new TopUpRequest(account.id, balance, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(topupRequest);
            }
        });

        btnRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = LoginActivity.getLoggedAccount();
                String name = NameStore.getText().toString();
                String address = AddressStore.getText().toString();
                String phone = PhoneStore.getText().toString();
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Register Store Sukses", Toast.LENGTH_SHORT).show();
                                l2.setVisibility(v.GONE);
                                l3.setVisibility(v.VISIBLE);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AboutMeActivity.this, "Register Store Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Register Store Gagal", Toast.LENGTH_SHORT).show();
                    }
                };

                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, name, address, phone, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(registerStoreRequest);
            }
        });
    }

    public void refreshBalance(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvBalance.setText("" + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutMeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }

    @Override
    protected void onResume() {
        refreshBalance();
        super.onResume();
    }

}