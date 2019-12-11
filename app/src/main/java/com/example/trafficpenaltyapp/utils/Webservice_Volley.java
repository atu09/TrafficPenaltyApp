package com.example.trafficpenaltyapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by MTAJ-08 on 12/5/2016.
 */
public class Webservice_Volley {

    RequestQueue requestQueue;
    Context context;

    private DataInterface da;

    ProgressDialog progressDialog;

    public Webservice_Volley(Context  cont,DataInterface da) {

        this.context=cont;
        requestQueue= Volley.newRequestQueue(context);
        this.da=da;

        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage("Loading");
    }


    public void CallVolley(String url,HashMap<String,String> map,final String tag)
    {


        progressDialog.show();

        try {

            JsonObjectRequest obreq;
            obreq = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            da.getData(response,tag);
                            progressDialog.dismiss();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.dismiss();
                            Toast.makeText(context, "errorr++"+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            obreq.setRetryPolicy(new DefaultRetryPolicy(600000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);

        }
        catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(context, "--" + e, Toast.LENGTH_SHORT).show();
        }

    }

    public void CallVolley_location(String url,HashMap<String,String> map,final String tag)
    {

        try {

            JsonObjectRequest obreq;
            obreq = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(map),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            da.getData(response,tag);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(context, "errorr++"+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            obreq.setRetryPolicy(new DefaultRetryPolicy(600000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);

        }
        catch (Exception e) {
            Toast.makeText(context, "--" + e, Toast.LENGTH_SHORT).show();
        }

    }

    }

