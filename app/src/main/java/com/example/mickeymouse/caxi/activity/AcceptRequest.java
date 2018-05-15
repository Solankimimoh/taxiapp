package com.example.mickeymouse.caxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.app.AppConfig;
import com.example.mickeymouse.caxi.app.AppController;
import com.example.mickeymouse.caxi.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AcceptRequest extends AppCompatActivity {

    private static final String TAG = ShareRequest.class.getSimpleName();

    private TextView shareUser;
    private ProgressDialog pDialog;
    private Button button;
    SessionManager sessionManager;
    String bookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);


        sessionManager = new SessionManager(AcceptRequest.this);

        pDialog = new ProgressDialog(this);

        bookid = getIntent().getStringExtra("bookid");
        String name = getIntent().getStringExtra("name");


        initview();


        shareUser.setText(name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.getInstance(AcceptRequest.this).isOnline()) {
                    sendaccept(bookid);

                } else {
                    Toast.makeText(AcceptRequest.this, "No Internet Connection ! Try Again", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }


    private void initview() {
        shareUser = (TextView) findViewById(R.id.requestuser);
        button = (Button) findViewById(R.id.accept_btn);


    }


    private void sendaccept(final String bookingid) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_SHARE_ACCEPT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Share_Req Response: " + response.toString());
                hideDialog();


                try {


                    JSONArray jsonArray = new JSONArray(response);

                    Toast.makeText(getApplicationContext(), "Your Ride Share with " + shareUser.getText(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AcceptRequest.this, HomeScreen.class);
                    startActivity(intent);


                    if (jsonArray.length() == 0) {
                        Toast.makeText(AcceptRequest.this, "No Data Available for Selected Date", Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

//
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("bookid", bookingid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
