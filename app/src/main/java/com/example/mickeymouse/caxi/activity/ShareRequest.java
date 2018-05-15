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
import com.example.mickeymouse.caxi.model.FilterData;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShareRequest extends AppCompatActivity {


    private static final String TAG = ShareRequest.class.getSimpleName();

    private TextView shareUser;
    private TextView sharePickup;
    private TextView shareDrop;
    private TextView shareCar;
    private TextView shareDate;
    private TextView shareTime;
    private ProgressDialog pDialog;
    private Button button;
    SessionManager sessionManager;
    String bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_request);

        sessionManager = new SessionManager(ShareRequest.this);

        pDialog = new ProgressDialog(this);

        bookid = getIntent().getStringExtra("bookid");

        if (AppConfig.getInstance(ShareRequest.this).isOnline()) {
            sendRequest(bookid);

        } else {
            Toast.makeText(ShareRequest.this, "No Internet Connection ! Try Again", Toast.LENGTH_LONG).show();
            finish();
        }

        initview();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppConfig.getInstance(ShareRequest.this).isOnline()) {
                    String sender = sessionManager.userNameSession();
                    String to = shareUser.getText().toString();

                    sendRequestforSharing(sender, to);

                } else {
                    Toast.makeText(ShareRequest.this, "No Internet Connection ! Try Again", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void initview() {

        shareUser = (TextView) findViewById(R.id.share_username);
        sharePickup = (TextView) findViewById(R.id.share_pickup);
        shareDrop = (TextView) findViewById(R.id.share_drop);
        shareCar = (TextView) findViewById(R.id.share_cartype);
        shareDate = (TextView) findViewById(R.id.share_day);
        shareTime = (TextView) findViewById(R.id.share_time);
        button = (Button) findViewById(R.id.share_req_btn);
    }

    public String getLocation(Double latitude, Double logntitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            addresses = geocoder.getFromLocation(latitude, logntitude, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private void sendRequest(final String bookingid) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET__BOOKID_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Share_Req Response: " + response.toString());
                hideDialog();


                try {


                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(ShareRequest.this, "No Data Available for Selected Date", Toast.LENGTH_LONG).show();
                        finish();
                    } else {


                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String pickupLocation = getCompleteAddressString(Double.parseDouble(jsonObject.getString("pick_loc_lat")), Double.parseDouble(jsonObject.getString("pick_loc_log")));

                            String DropLocation = getCompleteAddressString(Double.parseDouble(jsonObject.getString("drop_loc_lat")), Double.parseDouble(jsonObject.getString("drop_loc_log")));

                            shareUser.setText(jsonObject.getString("user_id"));
                            sharePickup.setText(pickupLocation);
                            shareDrop.setText(DropLocation);
                            shareCar.setText(jsonObject.getString("car_type"));
                            shareDate.setText(jsonObject.getString("pick_date"));
                            shareTime.setText(jsonObject.getString("pick_time"));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }


                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

//                try {
//                    JSONObject jObj = new JSONObject(response);
//
//                    Log.e(TAG, jObj.getString("pick_loc_lat"));
//
//
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
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
                params.put("bookid", bookid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void sendRequestforSharing(final String sender, final String to) {
        // Tag used to cancel the request
        String tag_string_req = "req_details";

        pDialog.setMessage("Loading in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_SHARE_REQUEST_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Share_Req Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "Request Send Suceesfully", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(ShareRequest.this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

//                try {
//                    JSONObject jObj = new JSONObject(response);
//
//                    Log.e(TAG, jObj.getString("pick_loc_lat"));
//
//
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
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
                params.put("sender", sender);
                params.put("to", to);

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
