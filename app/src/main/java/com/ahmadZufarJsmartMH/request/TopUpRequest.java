package com.ahmadZufarJsmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Merupakan Class untuk memberikan request terhadap backend mengenai topup pada balance
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class TopUpRequest extends StringRequest {
    private static final String URL = "http://192.168.100.10:1805/account/%d/topUp";
    private final Map<String, String> params;

    public TopUpRequest(int id, double balance, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST, String.format(URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("balance", String.valueOf(balance));

    }
    public Map<String, String> getParams(){

        return params;
    }
}
