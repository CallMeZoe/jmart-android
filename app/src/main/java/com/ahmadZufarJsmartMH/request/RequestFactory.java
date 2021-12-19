package com.ahmadZufarJsmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Merupakan Class untuk mendapatkan list object berdasarkan Id ataupun Page
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class RequestFactory {
    private static final String URL_FORMAT_ID = "http://192.168.100.10:1805/%s/%d";
    private static final String URL_FORMAT_PAGE = "http://192.168.100.10:1805/%s/page?page=%s&pageSize=%s";

    public static StringRequest getById(String parentURI, int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_ID, parentURI, id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public static StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
}
