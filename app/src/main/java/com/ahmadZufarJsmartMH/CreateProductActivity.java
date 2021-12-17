package com.ahmadZufarJsmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahmadZufarJsmartMH.model.Account;
import com.ahmadZufarJsmartMH.model.Product;
import com.ahmadZufarJsmartMH.model.ProductCategory;
import com.ahmadZufarJsmartMH.request.CreateProductRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Merupakan Class yang mengatur Activity Create Product untuk membuat produk baru
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class CreateProductActivity extends AppCompatActivity {

    private String[] Category = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};

    private Spinner categoryPlan;
    private Spinner ShipmentPlan;
    private EditText nameProduct;
    private EditText weightProduct;
    private EditText priceProduct;
    private EditText discountProduct;
    private RadioButton conditionProduct;
    private RadioButton conditionProduct2;
    private boolean valueCondition;
    private Button createProduct;
    private Account account;
    private Product product;
    private static final Gson gson = new Gson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        nameProduct = findViewById(R.id.nameCreateProduct);
        weightProduct = findViewById(R.id.weightCreateProduct);
        priceProduct = findViewById(R.id.priceCreateProduct);
        discountProduct = findViewById(R.id.discountCreateProduct);
        conditionProduct = findViewById(R.id.radiobtnConditionCreateProductNew);
        conditionProduct2 = findViewById(R.id.radiobtnConditionCreateProductUsed);
        createProduct = findViewById(R.id.butCreateProduct);

        categoryPlan = findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryPlan.setAdapter(adapter1);

        ShipmentPlan = findViewById(R.id.spinnerShipmentplans);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.shipmentPlans, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ShipmentPlan.setAdapter(adapter2);

//        if (conditionProduct.isChecked()){
//            valueCondition = true;
//        }
//        else if (conditionProduct2.isChecked()){
//            valueCondition = false;
//        }


        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                product = gson.fromJson(response, Product.class);
                                Toast.makeText(CreateProductActivity.this, "Create Product Sukses", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateProductActivity.this, "Create Product Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateProductActivity.this, "Create Product Gagal", Toast.LENGTH_SHORT).show();
                    }
                };
                account = LoginActivity.getLoggedAccount();
                String name = nameProduct.getText().toString();
                int weight = Integer.valueOf(weightProduct.getText().toString()) ;
                boolean conditionUsed = checkConditionRadioButton(conditionProduct, conditionProduct2);
                double price = Double.valueOf(priceProduct.getText().toString());
                double discount = Double.valueOf(discountProduct.getText().toString());
                String productCategory = categoryPlan.getSelectedItem().toString();
                ProductCategory Pcategory = ProductCategory.BOOK;
                for (ProductCategory p : ProductCategory.values()){
                    if (p.toString().equals(productCategory) ){
                        Pcategory = p;
                    }
                }
                String ShipmentPlansSpinner = ShipmentPlan.getSelectedItem().toString();
                byte valueShipment = 0;
                if (ShipmentPlansSpinner.equals("INSTANT")){
                    valueShipment = 1;
                }
                else if (ShipmentPlansSpinner.equals("SAME DAY")){
                    valueShipment = 2;
                }
                else if (ShipmentPlansSpinner.equals("NEXT DAY")){
                    valueShipment = 4;
                }
                else if (ShipmentPlansSpinner.equals("REGULER")){
                    valueShipment = 8;
                }
                else if (ShipmentPlansSpinner.equals("KARGO")){
                    valueShipment = 16;
                }
                CreateProductRequest createProductRequest = new CreateProductRequest(account.id, name, weight, conditionUsed, price, discount, Pcategory, valueShipment, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
                queue.add(createProductRequest);
            }
        });

    }

    public boolean checkConditionRadioButton(RadioButton conditionProduct, RadioButton conditionProduct2 ){
        boolean i = false;
        if(conditionProduct.isChecked()){
            i = true;
        }
        else if(conditionProduct2.isChecked()){
            i = false;
        }
        return i;
    }

}