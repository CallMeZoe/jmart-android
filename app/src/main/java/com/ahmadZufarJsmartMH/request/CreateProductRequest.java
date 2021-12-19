package com.ahmadZufarJsmartMH.request;

import com.ahmadZufarJsmartMH.model.ProductCategory;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Merupakan Class untuk memberikan request terhadap backend mengenai pembuatan product
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://192.168.100.10:1805/product/create";
    private final Map<String, String> params;

    public CreateProductRequest(int id, String NameProduct, int WeightProduct, boolean ConditionProduct, double priceProduct, double discountProduct, ProductCategory productCategory, byte shipmentPlans, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(id));
        params.put("name", NameProduct);
        params.put("weight", String.valueOf(WeightProduct));
        params.put("conditionUsed", String.valueOf(ConditionProduct));
        params.put("price", String.valueOf(priceProduct));
        params.put("discount", String.valueOf(discountProduct));
        params.put("category", productCategory.toString());
        params.put("shipmentPlans", String.valueOf(shipmentPlans));
    }

    public Map<String, String> getParams(){

        return params;
    }
}
