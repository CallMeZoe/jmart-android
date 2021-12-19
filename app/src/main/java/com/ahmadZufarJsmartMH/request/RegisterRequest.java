package com.ahmadZufarJsmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Merupakan Class untuk memberikan request terhadap backend mengenai register suatu account
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class RegisterRequest extends StringRequest {
    private static final String URL = "http://192.168.100.10:1805/account/register";
    private final Map<String,String> params;

    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
