package com.traffic.penalty.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.traffic.penalty.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import atirek.pothiwala.utility.helper.Loader;

public class VolleyCall {

    private RequestQueue requestQueue;
    private Context context;

    private DataCallListener dataCallListener;

    private Dialog progressDialog;

    public VolleyCall(Context context, DataCallListener dataCallListener) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(this.context);
        this.dataCallListener = dataCallListener;

        Loader loader = new Loader(context);
        loader.setColor(R.color.colorPrimary);
        loader.setCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                requestQueue.cancelAll("post");
                requestQueue.cancelAll("multipart");
            }
        });
        progressDialog = loader.getDialog();
    }


    public void CallVolleyRequest(String url, HashMap<String, String> params, final String tag) {

        try {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(tag + ">>", "Response: " + response.toString());
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            dataCallListener.OnData(response, tag);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d(tag + ">>", "Error: " + error.getMessage());
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(600000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag("post");
            requestQueue.add(request);

        } catch (Exception e) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void CallVolleyUpload(String url, final HashMap<String, String> params, final Map<String, VolleyMultipartRequest.DataPart> dataParams, final String tag) {

        try {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String json = new String(response.data);
                    Log.d(tag + ">>", "Response: " + response.toString());
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    try {
                        dataCallListener.OnData(new JSONObject(json), tag);
                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.d(tag + ">>", "Error: " + e.getMessage());
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(tag + ">>", "Error: " + error.getMessage());
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    return dataParams;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(600000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setTag("multipart");
            requestQueue.add(request);

        } catch (Exception e) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

