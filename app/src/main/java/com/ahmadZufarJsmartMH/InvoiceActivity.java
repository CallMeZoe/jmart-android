package com.ahmadZufarJsmartMH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadZufarJsmartMH.model.Account;
import com.ahmadZufarJsmartMH.model.Payment;
import com.ahmadZufarJsmartMH.request.InvoiceRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Merupakan Class yang mengatur Activity Invoice untuk menampilkan invoice
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class InvoiceActivity extends AppCompatActivity {

    private RecyclerView rvInvoice;
    private static final Gson gson = new Gson();
    private ArrayList<Payment> userInvoiceList = new ArrayList<>();
    private ArrayList<Payment> storeInvoiceList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private MenuItem itemPerson, itemStore;
    private TextView invoiceTitle;
    private Button invoiceStoreButton;
    private Button invoiceUserButton;
    public static boolean isUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        rvInvoice = findViewById(R.id.invoiceCardview);
        invoiceTitle = findViewById(R.id.invoiceTitle);
        invoiceStoreButton = findViewById(R.id.invoiceStoreButton);
        invoiceUserButton = findViewById(R.id.invoiceUserButton);


        getUserInvoiceList();

        invoiceStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceTitle.setText("Invoice Store History");
                isUser = false;
                getStoreInvoiceList();
                invoiceStoreButton.setVisibility(View.GONE);
                invoiceUserButton.setVisibility(View.VISIBLE);
            }
        });

        invoiceUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceTitle.setText("Invoice User History");
                isUser = true;
                getUserInvoiceList();
                invoiceUserButton.setVisibility(View.GONE);
                invoiceStoreButton.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getUserInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    userInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();     //mengambil tipe list Payment
                    userInvoiceList = gson.fromJson(response, paymentListType);
                    rvInvoice.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceCardViewAdapter invoiceCardViewAdapter = new InvoiceCardViewAdapter(userInvoiceList);
                    rvInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {     //jika response null
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, true, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(invoiceRequest);
    }

    private void getStoreInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    storeInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();     //mengambil tipe list Payment
                    storeInvoiceList = gson.fromJson(response, paymentListType);
                    rvInvoice.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceCardViewAdapter invoiceCardViewAdapter = new InvoiceCardViewAdapter(storeInvoiceList);
                    rvInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {     //jika response null
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, false, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(invoiceRequest);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.invoice_menu, menu);
//        itemPerson = menu.findItem(R.id.user);
//        itemStore = menu.findItem(R.id.store);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
//        setActivityMode(menuItem.getItemId());
//        return super.onOptionsItemSelected(menuItem);
//    }
//
//    public void setActivityMode(int modeSelected) {
//        switch (modeSelected) {
//            case R.id.store:
//                itemPerson.setVisible(true);
//                itemStore.setVisible(false);
//                invoiceTitle.setText("Invoice Store");
//                isUser = false;
//                getStoreInvoiceList();
//                break;
//            case R.id.user:
//                itemStore.setVisible(true);
//                itemPerson.setVisible(false);
//                invoiceTitle.setText("Invoice User");
//                isUser = true;
//                getUserInvoiceList();
//                break;
//            default:
//                break;
//        }
//    }

}