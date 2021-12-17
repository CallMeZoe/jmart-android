package com.ahmadZufarJsmartMH;

import android.app.Dialog;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadZufarJsmartMH.model.Account;
import com.ahmadZufarJsmartMH.model.Invoice;
import com.ahmadZufarJsmartMH.model.Payment;
import com.ahmadZufarJsmartMH.model.Product;
import com.ahmadZufarJsmartMH.request.PaymentRequest;
import com.ahmadZufarJsmartMH.request.RequestFactory;
import com.ahmadZufarJsmartMH.request.TopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Merupakan Class yang mengatur Adapter pada cardview di Activity Invoice
 * @author Ahmad Zufar A
 * @version 17 Desember 2021
 */

public class InvoiceCardViewAdapter extends RecyclerView.Adapter<InvoiceCardViewAdapter.InvoiceCardViewViewHolder> {
    private ArrayList<Payment> listPayment = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();
    private Dialog dialog;
    private Payment.Record lastRecord;
    private Account account = LoginActivity.getLoggedAccount();

    public InvoiceCardViewAdapter(ArrayList<Payment> list) {
        this.listPayment = list;
    }

    @NonNull
    @Override
    public InvoiceCardViewAdapter.InvoiceCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_invoice, viewGroup, false);
        return new InvoiceCardViewAdapter.InvoiceCardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceCardViewViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        lastRecord = payment.history.get(payment.history.size() - 1);
        getProductData(holder, payment);
        holder.noInvoice.setText("#" + payment.id);
        holder.invoiceStatus.setText(lastRecord.status.toString());
        holder.invoiceDate.setText(lastRecord.date.toString());
        holder.invoiceAddress.setText(payment.shipment.address);
        holder.layoutConfirmation.setVisibility(View.GONE);
        holder.buttonSubmit.setVisibility(View.GONE);

        if (InvoiceActivity.isUser) {
            holder.layoutConfirmation.setVisibility(View.GONE);
        } else {
            if (lastRecord.status.equals(Invoice.Status.WAITING_CONFIRMATION)) {
                holder.layoutConfirmation.setVisibility(View.VISIBLE);
            }
        }
        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerAcceptPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response dalam bentuk boolean
                        if (isAccepted) {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Accepted!", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.ON_PROGRESS, "Payment Accepted!"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirmation.setVisibility(View.GONE);
                            holder.buttonSubmit.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Accept failed, Payment cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerAcceptPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest acceptPaymentRequest = new PaymentRequest(payment.id, listenerAcceptPayment, errorListenerAcceptPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                queue.add(acceptPaymentRequest);
            }
        });

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerCancelPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response berbentuk boolean pertanda keberhasilan untuk melakukan cancel payment
                        if (isAccepted) {
                            double price = Double.valueOf(holder.invoiceCost.getText().toString().trim().substring(3));
                            Response.Listener<String> listener = new Response.Listener<String>() {      //listener top up
                                @Override
                                public void onResponse(String response) {
                                    Boolean object = Boolean.valueOf(response);
                                    if (object) {
                                        Toast.makeText(holder.InvoiceCardview.getContext(), "Balance has been returned", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.InvoiceCardview.getContext(), "Balance can't be returned", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                                }
                            };
                            TopUpRequest topUpRequest = new TopUpRequest(payment.buyerId, price, listener, errorListener);
                            RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                            queue.add(topUpRequest);
                            Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.CANCELLED, "Payment Cancelled!"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirmation.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(holder.InvoiceCardview.getContext(), "This payment can't be cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerCancelPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest cancelPaymentRequest = new PaymentRequest(listenerCancelPayment, payment.id, errorListenerCancelPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                queue.add(cancelPaymentRequest);
            }
        });

        holder.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(holder.buttonAccept.getContext());
                dialog.setContentView(R.layout.dialog_receipt);
                EditText editRecipt = dialog.findViewById(R.id.editReceipt);
                Button buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
                dialog.show();
                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dataRecipt = editRecipt.getText().toString().trim();
                        Response.Listener<String> listenerSubmitPayment = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Boolean isSubmitted = Boolean.valueOf(response);
                                if (isSubmitted) {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "Payment Submitted", Toast.LENGTH_SHORT).show();
                                    payment.history.add(new Payment.Record(Invoice.Status.ON_DELIVERY, "Payment has been Submitted"));
                                    lastRecord = payment.history.get(payment.history.size() - 1);
                                    holder.invoiceStatus.setText(lastRecord.status.toString());
                                    holder.invoiceReceipt.setText(payment.shipment.receipt);
                                    holder.buttonSubmit.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(holder.InvoiceCardview.getContext(), "This payment can't be submitted! " + payment.id, Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        Response.ErrorListener errorListenerSubmitPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(holder.InvoiceCardview.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                            }
                        };
                        PaymentRequest submitPaymentRequest = new PaymentRequest(payment.id, dataRecipt, listenerSubmitPayment, errorListenerSubmitPayment);
                        RequestQueue queue = Volley.newRequestQueue(holder.InvoiceCardview.getContext());
                        queue.add(submitPaymentRequest);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPayment.size();
    }

    class InvoiceCardViewViewHolder extends RecyclerView.ViewHolder {
        TextView noInvoice, invoiceName, invoiceStatus, invoiceDate, invoiceAddress, invoiceCost, invoiceReceipt;
        ImageButton expandButton;
        RecyclerView rvHistory;
        LinearLayout historyLayout;
        CardView InvoiceCardview;
        Button buttonAccept, buttonSubmit, buttonCancel;
        LinearLayout layoutConfirmation;

        InvoiceCardViewViewHolder(View itemView) {
            super(itemView);
            noInvoice = itemView.findViewById(R.id.noInvoice);
            invoiceName = itemView.findViewById(R.id.invoiceName);
            invoiceStatus = itemView.findViewById(R.id.invoiceStatus);
            invoiceDate = itemView.findViewById(R.id.invoiceDate);
            invoiceAddress = itemView.findViewById(R.id.invoiceAddress);
            invoiceCost = itemView.findViewById(R.id.invoiceCost);
            invoiceReceipt = itemView.findViewById(R.id.invoiceReceipt);
            InvoiceCardview = itemView.findViewById(R.id.invoiceCardView);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonSubmit = itemView.findViewById(R.id.buttonSubmit);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            layoutConfirmation = itemView.findViewById(R.id.layoutConfirmation);
        }
    }

    public void getProductData(InvoiceCardViewAdapter.InvoiceCardViewViewHolder holder, Payment payment) {
        Response.Listener<String> listenerProduct = new Response.Listener<String>() {       //listener
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);
                    holder.invoiceName.setText(product.name);
                    holder.invoiceCost.setText("Rp. " + ((product.price-(product.price * (product.discount / (100)))) * payment.productCount));
                    holder.invoiceReceipt.setText(payment.shipment.receipt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.noInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.noInvoice.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, listenerProduct, errorListener));        //memasukkan request untuk mengambil informasi payment berdasarkan id
    }


}

