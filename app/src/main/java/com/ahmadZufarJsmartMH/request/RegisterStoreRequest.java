package com.ahmadZufarJsmartMH.request;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterStoreRequest extends StringRequest {
    private static final String URL = "http://192.168.100.10:1805/account/%d/registerStore";
    private final Map<String, String> params;

    public RegisterStoreRequest(int id, String storeName, String storeAddress, String storePhoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("name", storeName);
        params.put("address", storeAddress);
        params.put("phoneNumber", storePhoneNumber);
    }

    public Map<String, String> getParams(){

        return params;
    }

}
