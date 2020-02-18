package com.example.trafficpenaltyapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by MTAJ-08 on 12/5/2016.
 */
public class VolleyCall {

    private RequestQueue requestQueue;
    private Context context;

    private DataCallListener dataCallListener;

    private ProgressDialog progressDialog;

    public VolleyCall(Context context, DataCallListener dataCallListener) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(this.context);
        this.dataCallListener = dataCallListener;

        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
    }


    public void CallVolley(String url, HashMap<String, String> map, final String tag) {

        try {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map),
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
            requestQueue.add(request);

        } catch (Exception e) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}

