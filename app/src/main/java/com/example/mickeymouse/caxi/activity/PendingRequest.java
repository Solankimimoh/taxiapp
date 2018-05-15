package com.example.mickeymouse.caxi.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mickeymouse.caxi.R;
import com.example.mickeymouse.caxi.adapter.CustomListAdapter;
import com.example.mickeymouse.caxi.adapter.ReqAccept;
import com.example.mickeymouse.caxi.app.AppConfig;
import com.example.mickeymouse.caxi.app.AppController;
import com.example.mickeymouse.caxi.helper.SessionManager;
import com.example.mickeymouse.caxi.model.FilterData;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingRequest extends AppCompatActivity {


    private static final String TAG = ShareRideListView.class.getSimpleName();

    LatLng pickupLatLng;
    LatLng dropLatLng;
    String pickup_date_tx;
    private ProgressDialog pDialog;
    private SessionManager session;
    String username;

    private List<FilterData> filterDataArrayList = new ArrayList<FilterData>();
    private ListView listView;
    private ReqAccept adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_request);


        session = new SessionManager(PendingRequest.this);

        username = session.userNameSession();

        listView = (ListView) findViewById(R.id.list_pending_request);
        adapter = new ReqAccept(this, filterDataArrayList);
        listView.setAdapter(adapter);



        pDialog = new ProgressDialog(this);




        if (AppConfig.getInstance(PendingRequest.this).isOnline()) {
            getridedetails(username);

        }
    }


    /**
     * function to verify login details in mysql db
     */
    private void getridedetails(final String user) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_SHARE_REQUEST_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(PendingRequest.this, "No Data Available for Selected Date", Toast.LENGTH_LONG).show();
                        finish();
                    } else {


                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            FilterData filterData = new FilterData();

                            filterData.setBookid(jsonObject.getString("share_id"));
                            filterData.setUserName(jsonObject.getString("share_req_user"));
                            filterData.setCarType("");

                            String status =jsonObject.getString("is_share");

                            if(status.equals("0"))
                            {
                                filterData.setTime("Pending..");
                            }



                            filterDataArrayList.add(filterData);


                            Log.e(TAG, "TIME" + jsonObject.getString("user_id"));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

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
                params.put("user", user);

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
