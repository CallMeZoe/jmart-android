package com.ahmadZufarJsmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestFactory {
    private final String URL_FORMAT_ID = "http://192.168.100.10:1805/%s/%d";
    private final String URL_FORMAT_PAGE = "http://192.168.100.10:1805%s/page";

    public final StringRequest getById(String parentURI, int id, Response.Listener<String>listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_ID,parentURI,id);
        return new StringRequest(Request.Method.GET,url,listener,errorListener);
    }

    public final StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String>listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_PAGE,parentURI);
        Map<String, String> params = new HashMap<>();
        params.put("page",String.format(URL_FORMAT_PAGE,parentURI));
        params.put("pageSize",String.valueOf(pageSize));
        return new StringRequest(Request.Method.GET,url,listener,errorListener){
            public Map<String, String> getParams(){
                return params;
            }
        };
    }
}
